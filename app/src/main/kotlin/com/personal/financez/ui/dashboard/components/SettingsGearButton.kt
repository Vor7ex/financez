package com.personal.financez.ui.dashboard.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/**
 * Ícono de engranaje limpio y minimalista para acceder a la configuración de la app.
 * Sin contenedor circular ni borde, con alto contraste sobre el fondo oscuro y animación
 * elástica que aumenta sutilmente su tamaño al hacer hover o pulsarlo.
 */
@Composable
fun SettingsGearButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isInteracting = isPressed || isHovered

    // Animación que aumenta ligeramente el tamaño en hover/clic (de 1.0f a 1.12f)
    val gearScale by animateFloatAsState(
        targetValue = if (isInteracting) 1.12f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "settingsGearScale"
    )

    // Alto contraste sobre el fondo (FrostedIceMint por defecto, VibrantMint en hover/pulsación)
    val animatedIconColor by animateColorAsState(
        targetValue = if (isInteracting) colors.primary else colors.onBackground,
        label = "settingsGearIconColor"
    )

    Box(
        modifier = modifier
            .size(44.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Configuración de la aplicación",
            tint = animatedIconColor,
            modifier = Modifier
                .size(24.dp)
                .graphicsLayer {
                    scaleX = gearScale
                    scaleY = gearScale
                }
        )
    }
}
