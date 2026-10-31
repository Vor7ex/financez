package com.personal.financez.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.personal.financez.di.AppContainer

/**
 * Contrato de orquestación de navegación para features independientes.
 * Cada feature implementa esta interfaz para registrar sus propios destinos
 * en el NavGraphBuilder sin exponer pantallas internas ni ViewModels a MainActivity.
 */
interface FeatureNavGraph {
    fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        appContainer: AppContainer
    )
}
