package com.personal.financez.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.personal.financez.di.AppContainer
import com.personal.financez.ui.dashboard.DashboardNavGraph
import com.personal.financez.ui.onboarding.OnboardingNavGraph
import kotlinx.coroutines.flow.first

private sealed interface InitialDestinationState {
    data object Determining : InitialDestinationState
    data class Resolved(val startDestination: String) : InitialDestinationState
}

/**
 * Host de navegación raíz de la aplicación.
 * Orquesta los destinos declarativos provistos por cada FeatureNavGraph
 * sin acoplar detalles de pantallas ni ViewModels de features específicas.
 */
@Composable
fun AppNavHost(
    appContainer: AppContainer,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    featureNavGraphs: List<FeatureNavGraph> = remember {
        listOf(
            OnboardingNavGraph(),
            DashboardNavGraph()
        )
    }
) {
    val destinationState by produceState<InitialDestinationState>(
        initialValue = InitialDestinationState.Determining,
        key1 = appContainer
    ) {
        val configuredCurrency = appContainer.preferencesRepository.baseCurrency.first()
        val startDestination = if (configuredCurrency == null) {
            NavRoutes.ONBOARDING
        } else {
            NavRoutes.DASHBOARD
        }
        value = InitialDestinationState.Resolved(startDestination)
    }

    when (val state = destinationState) {
        is InitialDestinationState.Determining -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        is InitialDestinationState.Resolved -> {
            NavHost(
                navController = navController,
                startDestination = state.startDestination,
                modifier = modifier
            ) {
                featureNavGraphs.forEach { feature ->
                    feature.registerGraph(
                        navGraphBuilder = this,
                        navController = navController,
                        appContainer = appContainer
                    )
                }
            }
        }
    }
}
