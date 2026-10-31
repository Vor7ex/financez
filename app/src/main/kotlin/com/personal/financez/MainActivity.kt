package com.personal.financez

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.personal.financez.ui.navigation.AppNavHost
import com.personal.financez.ui.theme.FinanceZTheme

/**
 * Actividad principal y punto de entrada visual.
 * Actúa como host pasivo desacoplado: solo inicializa el tema y delega
 * la orquestación visual completa a AppNavHost y sus FeatureNavGraphs.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FinanceZTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val app = application as FinanceZApplication
                    AppNavHost(appContainer = app.container)
                }
            }
        }
    }
}
