package com.personal.financez

import android.app.Application
import com.personal.financez.di.AppContainer
import com.personal.financez.di.DefaultAppContainer

/**
 * Instancia de aplicación que vive durante todo el ciclo del proceso.
 * Solo inicializa el contenedor de infraestructura central sin alojar lógica de dependencias.
 */
class FinanceZApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
