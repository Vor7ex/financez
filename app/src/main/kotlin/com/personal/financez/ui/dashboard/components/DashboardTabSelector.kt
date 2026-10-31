package com.personal.financez.ui.dashboard.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.personal.financez.ui.dashboard.DashboardTab

/**
 * Selector de pestañas tipo cápsula (stadium) para alternar entre "Patrimonio" y "Cuentas".
 * Posee extremos completamente redondeados (circulares) con una píldora indicadora deslizante
 * que rodea con suavidad táctil la opción activa, con texto en negrilla y soporte para gestos de arrastre.
 */
@Composable
fun DashboardTabSelector(
    selectedTab: DashboardTab,
    onTabSelected: (DashboardTab) -> Unit,
    modifier: Modifier = Modifier,
    pagerState: PagerState? = null
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    // Contenedor exterior en forma de cápsula con extremos circulares y detector de deslizamiento
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(CircleShape)
            .background(colors.surface)
            .pointerInput(selectedTab) {
                detectHorizontalDragGestures { change, dragAmount ->
                    change.consume()
                    if (dragAmount > 15f && selectedTab == DashboardTab.PATRIMONIO) {
                        onTabSelected(DashboardTab.CUENTAS)
                    } else if (dragAmount < -15f && selectedTab == DashboardTab.CUENTAS) {
                        onTabSelected(DashboardTab.PATRIMONIO)
                    }
                }
            }
            .padding(4.dp)
    ) {
        val calculatedBias = if (pagerState != null) {
            val offset = pagerState.currentPage + pagerState.currentPageOffsetFraction
            (offset * 2f - 1f).coerceIn(-1f, 1f)
        } else {
            val targetHorizontalBias = if (selectedTab == DashboardTab.PATRIMONIO) -1f else 1f
            val animatedBias by animateFloatAsState(
                targetValue = targetHorizontalBias,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMediumLow
                ),
                label = "tab_selector_bias_anim"
            )
            animatedBias
        }

        // Subcomponente píldora que rodea la opción seleccionada actualmente
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .align(BiasAlignment(horizontalBias = calculatedBias, verticalBias = 0f))
                .clip(CircleShape)
                .background(colors.secondary)
        )

        // Pestañas interactivas de texto ("Patrimonio" y "Cuentas") con tipografía en negrilla
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            val tabs = listOf(
                DashboardTab.PATRIMONIO to "Patrimonio",
                DashboardTab.CUENTAS to "Cuentas"
            )

            tabs.forEach { (tab, label) ->
                val isSelected = selectedTab == tab
                val animatedTextColor by animateColorAsState(
                    targetValue = if (isSelected) colors.onSecondary else colors.onSurfaceVariant,
                    label = "tab_text_color_anim"
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
                            onTabSelected(tab)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        style = typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = animatedTextColor
                    )
                }
            }
        }
    }
}
