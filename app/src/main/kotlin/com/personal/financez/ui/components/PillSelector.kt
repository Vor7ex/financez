package com.personal.financez.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Selector tipo píldora (cápsula deslizable) reutilizable.
 *
 * @param items Lista de opciones a mostrar en la píldora.
 * @param selectedIndex Índice del elemento actualmente seleccionado.
 * @param onSelect Callback invocado al seleccionar un índice.
 * @param labelProvider Función que convierte un elemento [T] en su etiqueta de texto.
 * @param overrideHorizontalBias Sesgo horizontal opcional para sincronizar directamente con gestos como Pager.
 */
@Composable
fun <T> PillSelector(
    items: List<T>,
    selectedIndex: Int,
    onSelect: (T) -> Unit,
    labelProvider: (T) -> String,
    modifier: Modifier = Modifier,
    overrideHorizontalBias: Float? = null
) {
    if (items.isEmpty()) return

    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(CircleShape)
            .background(colors.surface)
            .padding(3.dp)
    ) {
        val targetBias = if (items.size > 1) {
            val fraction = selectedIndex.toFloat() / (items.size - 1).toFloat()
            fraction * 2f - 1f
        } else 0f

        val animatedBias by animateFloatAsState(
            targetValue = overrideHorizontalBias ?: targetBias,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow
            ),
            label = "pill_selector_bias_anim"
        )

        val itemWidthFraction = 1f / items.size.toFloat()

        // Píldora indicadora deslizante
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(itemWidthFraction)
                .align(BiasAlignment(horizontalBias = animatedBias, verticalBias = 0f))
                .clip(CircleShape)
                .background(colors.secondary)
        )

        // Opciones interactivas
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex
                val animatedTextColor by animateColorAsState(
                    targetValue = if (isSelected) colors.onSecondary else colors.onSurfaceVariant,
                    label = "pill_text_color_anim"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            onSelect(item)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = labelProvider(item),
                        style = typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = animatedTextColor
                    )
                }
            }
        }
    }
}
