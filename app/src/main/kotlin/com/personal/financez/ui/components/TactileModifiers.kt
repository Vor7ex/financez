package com.personal.financez.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

/**
 * Modificador táctil reutilizable que aplica una escala elástica suave (Spring)
 * ante eventos de hover y pulsación sin dependencias visuales directas.
 *
 * @param targetScale Escala destino durante la interacción (ej. 0.96f para hundimiento, 1.10f para realce).
 * @param onClick Acción opcional al hacer clic/tap sin efecto ripple invasivo.
 */
fun Modifier.tactileScale(
    targetScale: Float,
    onClick: (() -> Unit)? = null
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isInteracting = isPressed || isHovered

    val scale by animateFloatAsState(
        targetValue = if (isInteracting) targetScale else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "tactileScaleAnim"
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .then(
            if (onClick != null) {
                Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                )
            } else {
                Modifier
            }
        )
}

/**
 * Efecto de hundimiento táctil en pulsación (escala a 0.96f).
 */
fun Modifier.tactilePress(
    targetScale: Float = 0.96f,
    onClick: () -> Unit
): Modifier = tactileScale(targetScale = targetScale, onClick = onClick)

/**
 * Efecto de crecimiento sutil en hover/pulsación (escala a 1.10f).
 */
fun Modifier.tactileBounce(
    targetScale: Float = 1.10f,
    onClick: () -> Unit
): Modifier = tactileScale(targetScale = targetScale, onClick = onClick)
