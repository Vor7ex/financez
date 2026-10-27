package com.personal.financez.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personal.financez.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface OnboardingUiState {
    data object Loading : OnboardingUiState
    data class SelectCurrency(val selectedCurrency: String = "COP") : OnboardingUiState
    data object Completed : OnboardingUiState
}

class OnboardingViewModel(
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<OnboardingUiState> = preferencesRepository.baseCurrency
        .map { currency ->
            if (currency == null) {
                OnboardingUiState.SelectCurrency()
            } else {
                OnboardingUiState.Completed
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = OnboardingUiState.SelectCurrency()
        )
    fun confirmCurrency(currencyCode: String) {
        viewModelScope.launch {
            preferencesRepository.setBaseCurrency(currencyCode)
        }
    }
}
