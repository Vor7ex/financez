package com.personal.financez.ui.dashboard.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Tarjeta de botón para "Nueva cuenta" que se posiciona en el primer espacio de la cuadrícula.
 * Posee un borde con línea punteada, sin relleno sólido (color de fondo) y animación táctil
 * elástica idéntica a los botones de selección de divisas de la app.
 */
@Composable
fun NewAccountPlaceholderCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isInteracting = isPressed || isHovered

    // Animación de rebote físico táctil idéntica a TactileCurrencyItem
    val cardScale by animateFloatAsState(
        targetValue = if (isInteracting) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "newAccountCardScale"
    )

    val animatedContentColor by animateColorAsState(
        targetValue = if (isInteracting) colors.primary else colors.onSurfaceVariant,
        label = "newAccountColorAnim"
    )

    val animatedBorderColor by animateColorAsState(
        targetValue = if (isInteracting) colors.primary else colors.outline.copy(alpha = 0.45f),
        label = "newAccountBorderColorAnim"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(115.dp)
            .graphicsLayer {
                scaleX = cardScale
                scaleY = cardScale
            }
            .clip(RoundedCornerShape(14.dp))
            .dashedBorder(
                strokeWidth = 1.5.dp,
                color = animatedBorderColor,
                cornerRadius = 14.dp,
                dashLength = 7.dp,
                gapLength = 6.dp
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Agregar cuenta",
                tint = animatedContentColor,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Nueva cuenta",
                style = typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = animatedContentColor
            )
        }
    }
}

/**
 * Modificador para dibujar un contorno de línea punteada con esquinas redondeadas.
 */
private fun Modifier.dashedBorder(
    strokeWidth: Dp,
    color: Color,
    cornerRadius: Dp,
    dashLength: Dp,
    gapLength: Dp
): Modifier = this.drawWithContent {
    drawContent()
    val strokeWidthPx = strokeWidth.toPx()
    val cornerRadiusPx = cornerRadius.toPx()
    val dashPx = dashLength.toPx()
    val gapPx = gapLength.toPx()

    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashPx, gapPx), 0f)
    val halfStroke = strokeWidthPx / 2f
    val rect = Rect(
        left = halfStroke,
        top = halfStroke,
        right = size.width - halfStroke,
        bottom = size.height - halfStroke
    )
    val roundRect = RoundRect(rect, CornerRadius(cornerRadiusPx, cornerRadiusPx))
    val path = Path().apply { addRoundRect(roundRect) }

    drawPath(
        path = path,
        color = color,
        style = Stroke(width = strokeWidthPx, pathEffect = pathEffect)
    )
}
