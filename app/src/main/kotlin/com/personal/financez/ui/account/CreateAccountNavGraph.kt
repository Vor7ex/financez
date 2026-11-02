package com.personal.financez.ui.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.personal.financez.data.DefaultAccountRepository
import com.personal.financez.di.AppContainer
import com.personal.financez.ui.navigation.FeatureNavGraph
import com.personal.financez.ui.navigation.NavRoutes

class CreateAccountNavGraph : FeatureNavGraph {

    override fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        appContainer: AppContainer
    ) {
        navGraphBuilder.composable(route = NavRoutes.CREATE_ACCOUNT) {
            val accountRepository = remember(appContainer) {
                DefaultAccountRepository(
                    institutionDao = appContainer.database.institutionDao(),
                    accountDao = appContainer.database.accountDao(),
                    context = appContainer.context
                )
            }

            val viewModel: CreateAccountViewModel = viewModel {
                CreateAccountViewModel(
                    accountRepository = accountRepository,
                    preferencesRepository = appContainer.preferencesRepository
                )
            }

            val state by viewModel.uiState.collectAsStateWithLifecycle()

            CreateAccountScreen(
                state = state,
                onNameChange = viewModel::updateName,
                onTypeChange = viewModel::updateType,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
