package com.personal.financez.ui.dashboard.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.personal.financez.ui.components.tactilePress

/**
 * Tarjeta de botón para "Nueva cuenta" que se posiciona en el primer espacio de la cuadrícula.
 * Posee un borde con línea punteada, sin relleno sólido y efecto de hundimiento táctil en pulsación
 * manteniendo un color constante y sobrio.
 */
@Composable
fun NewAccountPlaceholderCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(115.dp)
            .tactilePress(
                targetScale = 0.95f,
                onClick = onClick
            )
            .dashedBorder(
                strokeWidth = 1.5.dp,
                color = colors.outline.copy(alpha = 0.45f),
                cornerRadius = 14.dp,
                dashLength = 7.dp,
                gapLength = 6.dp
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
                tint = colors.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Nueva cuenta",
                style = typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = colors.onSurfaceVariant
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
