package com.personal.financez.ui.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.personal.financez.data.DefaultAccountRepository
import com.personal.financez.di.AppContainer
import com.personal.financez.ui.navigation.FeatureNavGraph
import com.personal.financez.ui.navigation.NavRoutes

class DashboardNavGraph : FeatureNavGraph {

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        appContainer: AppContainer
    ) {
        navGraphBuilder.composable(route = NavRoutes.DASHBOARD) {
            val accountRepository = remember(appContainer) {
                DefaultAccountRepository(
                    institutionDao = appContainer.database.institutionDao(),
                    accountDao = appContainer.database.accountDao(),
                    context = appContainer.context
                )
            }

            val dashboardViewModel: DashboardViewModel = viewModel {
                DashboardViewModel(
                    accountRepository = accountRepository,
                    preferencesRepository = appContainer.preferencesRepository
                )
            }

            val dashboardState by dashboardViewModel.uiState.collectAsStateWithLifecycle()

            DashboardScreenHost(
                state = dashboardState,
                onAddAccount = {
                    // Manejo de apertura de creación de cuenta
                }
            )
        }
    }
}

@Composable
private fun DashboardScreenHost(
    state: DashboardUiState,
    onAddAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        is DashboardUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        is DashboardUiState.Empty, is DashboardUiState.Success -> {
            // UI de cuentas/dashboard a definir en la fase de diseño de pantalla
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
