package com.personal.financez.ui.dashboard.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.financez.ui.components.tactileBounce

/**
 * Ícono de engranaje limpio y minimalista para acceder a la configuración de la app.
 * Sin contenedor ni cambio de color en hover: mantiene color constante de alto contraste
 * y animación táctil que aumenta sutilmente de tamaño al interactuar.
 */
@Composable
fun SettingsGearButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .size(44.dp)
            .tactileBounce(targetScale = 1.10f, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Configuración de la aplicación",
            tint = colors.onBackground,
            modifier = Modifier.size(24.dp)
        )
    }
}
