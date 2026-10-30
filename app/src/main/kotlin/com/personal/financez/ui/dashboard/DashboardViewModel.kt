package com.personal.financez.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.financez.data.AccountRepository
import com.personal.financez.data.local.AccountEntity
import com.personal.financez.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

enum class DashboardTab {
    PATRIMONIO,
    CUENTAS
}

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class Empty(val baseCurrency: String = "COP") : DashboardUiState
    data class Success(
        val accounts: List<AccountEntity>,
        val consolidatedNetWorth: Long,
        val baseCurrency: String = "COP",
        val selectedTab: DashboardTab = DashboardTab.PATRIMONIO
    ) : DashboardUiState
}

class DashboardViewModel(
    private val accountRepository: AccountRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val selectedTabFlow = MutableStateFlow(DashboardTab.PATRIMONIO)

    val uiState: StateFlow<DashboardUiState> = combine(
        accountRepository.observeAccounts(),
        accountRepository.observeConsolidatedNetWorth(),
        preferencesRepository.baseCurrency,
        selectedTabFlow
    ) { accounts, consolidatedNetWorth, baseCurrency, selectedTab ->
        val currency = baseCurrency ?: "COP"
        if (accounts.isEmpty()) {
            DashboardUiState.Empty(baseCurrency = currency)
        } else {
            DashboardUiState.Success(
                accounts = accounts,
                consolidatedNetWorth = consolidatedNetWorth,
                baseCurrency = currency,
                selectedTab = selectedTab
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = DashboardUiState.Loading
    )

    fun selectTab(tab: DashboardTab) {
        selectedTabFlow.value = tab
    }
}
