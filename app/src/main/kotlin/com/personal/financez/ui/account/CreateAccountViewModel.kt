package com.personal.financez.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.financez.data.AccountRepository
import com.personal.financez.data.local.AccountEntity
import com.personal.financez.data.local.AccountType
import com.personal.financez.data.local.FinancialInstitutionEntity
import com.personal.financez.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CreateAccountUiState(
    val name: String = "",
    val type: AccountType = AccountType.CUENTA_BANCARIA,
    val institutionId: String? = null,
    val institutions: List<FinancialInstitutionEntity> = emptyList(),
    val balanceInput: String = "",
    val currency: String = "COP",
    val annualEffectiveRateInput: String = "",
    val isGmfExempt: Boolean = false,
    val nameError: String? = null,
    val institutionError: String? = null,
    val isSubmitting: Boolean = false
)

class CreateAccountViewModel(
    private val accountRepository: AccountRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateAccountUiState())
    val uiState: StateFlow<CreateAccountUiState> = _uiState.asStateFlow()

    private val _dismissEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val dismissEvent: SharedFlow<Unit> = _dismissEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            val baseCurrency = preferencesRepository.baseCurrency.first() ?: "COP"
            _uiState.update { it.copy(currency = baseCurrency) }
        }
        viewModelScope.launch {
            accountRepository.seedInstitutionsIfEmpty()
            accountRepository.observeInstitutions().collect { list ->
                _uiState.update { it.copy(institutions = list) }
            }
        }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name, nameError = null) }
    }

    fun updateType(type: AccountType) {
        _uiState.update {
            it.copy(
                type = type,
                institutionId = if (type == AccountType.EFECTIVO) null else it.institutionId,
                institutionError = null
            )
        }
    }

    fun selectInstitution(institutionId: String?) {
        _uiState.update { it.copy(institutionId = institutionId, institutionError = null) }
    }

    fun updateBalance(balance: String) {
        _uiState.update { it.copy(balanceInput = balance) }
    }

    fun updateCurrency(currency: String) {
        _uiState.update { it.copy(currency = currency) }
    }

    fun updateAnnualEffectiveRate(rate: String) {
        _uiState.update { it.copy(annualEffectiveRateInput = rate) }
    }

    fun setGmfExempt(isExempt: Boolean) {
        _uiState.update { it.copy(isGmfExempt = isExempt) }
    }

    suspend fun submit(): Boolean {
        val currentState = _uiState.value

        var hasError = false
        if (currentState.name.isBlank()) {
            _uiState.update { it.copy(nameError = "El nombre de la cuenta es obligatorio") }
            hasError = true
        }

        if (currentState.type == AccountType.CUENTA_BANCARIA && currentState.institutionId.isNullOrBlank()) {
            _uiState.update { it.copy(institutionError = "Debes seleccionar una institución financiera") }
            hasError = true
        }

        if (hasError) return false

        _uiState.update { it.copy(isSubmitting = true) }

        val balanceInCents = parseAmountToCents(currentState.balanceInput)
        val rate = currentState.annualEffectiveRateInput
            .trim()
            .replace("%", "")
            .replace(",", ".")
            .toDoubleOrNull()

        val entity = AccountEntity(
            name = currentState.name.trim(),
            type = currentState.type,
            institutionId = if (currentState.type == AccountType.CUENTA_BANCARIA) currentState.institutionId else null,
            balance = balanceInCents,
            currency = currentState.currency,
            isGmfExempt = currentState.isGmfExempt,
            annualEffectiveRate = rate
        )

        accountRepository.createAccount(entity)
        _dismissEvent.tryEmit(Unit)
        _uiState.update { it.copy(isSubmitting = false) }
        return true
    }

    private fun parseAmountToCents(input: String): Long {
        val clean = input.trim().replace("$", "").replace(" ", "")
        if (clean.isBlank()) return 0L
        val normalized = clean.replace(",", ".")
        val parts = normalized.split(".")
        val integerPart = parts[0].filter { it.isDigit() }.ifBlank { "0" }.toLongOrNull() ?: 0L
        val decimalPart = if (parts.size > 1) {
            val decimals = parts[1].filter { it.isDigit() }.padEnd(2, '0').take(2)
            decimals.toLongOrNull() ?: 0L
        } else 0L
        return integerPart * 100L + decimalPart
    }
}
