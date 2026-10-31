package com.personal.financez.ui.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.personal.financez.di.AppContainer
import com.personal.financez.ui.navigation.FeatureNavGraph
import com.personal.financez.ui.navigation.NavRoutes

class OnboardingNavGraph : FeatureNavGraph {

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        appContainer: AppContainer
    ) {
        navGraphBuilder.composable(route = NavRoutes.ONBOARDING) {
            val viewModel: OnboardingViewModel = viewModel {
                OnboardingViewModel(appContainer.preferencesRepository)
            }

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            when (val state = uiState) {
                is OnboardingUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
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
                    LaunchedEffect(Unit) {
                        navController.navigate(NavRoutes.DASHBOARD) {
                            popUpTo(NavRoutes.ONBOARDING) { inclusive = true }
                        }
                    }
                }
            }
        }
    }
}
