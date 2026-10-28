package com.personal.financez

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.personal.financez.ui.onboarding.CurrencySelectionScreen
import com.personal.financez.ui.onboarding.OnboardingUiState
import com.personal.financez.ui.onboarding.OnboardingViewModel
import com.personal.financez.ui.theme.BackgroundDark
import com.personal.financez.ui.theme.FinanceZTheme
import com.personal.financez.ui.theme.MutedTeal
import com.personal.financez.ui.theme.TextWhite

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FinanceZTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundDark
                ) {
                    val app = application as FinanceZApplication
                    val viewModel: OnboardingViewModel = viewModel(
                        factory = object : ViewModelProvider.Factory {
                            @Suppress("UNCHECKED_CAST")
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return OnboardingViewModel(app.container.preferencesRepository) as T
                            }
                        }
                    )

                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                    when (val state = uiState) {
                        is OnboardingUiState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = MutedTeal)
                            }
                        }

                        is OnboardingUiState.SelectCurrency -> {
                            CurrencySelectionScreen(
                                initialCurrency = state.selectedCurrency,
                                onConfirm = { selectedCurrency ->
                                    viewModel.confirmCurrency(selectedCurrency)
                                }
                            )
                        }

                        is OnboardingUiState.Completed -> {
                            // Pantalla principal fuera de scope en esta historia
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Moneda base configurada con éxito",
                                    color = TextWhite
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
