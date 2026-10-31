package com.personal.financez.ui.dashboard.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

/**
 * Mensaje informativo cuando el patrimonio está en 0, con enlace interactivo subrayado
 * que se resalta como un marcador verdoso sobre "agrega una cuenta" en hover/pulsación.
 */
@Composable
fun EmptyNetWorthMessage(
    onNavigateToAccounts: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val isInteracting = isHovered || isPressed

    val highlightBackground by animateColorAsState(
        targetValue = if (isInteracting) colors.primary.copy(alpha = 0.22f) else Color.Transparent,
        label = "markerHighlightColor"
    )

    val annotatedText = buildAnnotatedString {
        withStyle(SpanStyle(color = colors.onSurfaceVariant)) {
            append("Aún no tienes patrimonio registrado, ")
        }
        withStyle(
            SpanStyle(
                color = colors.primary,
                background = highlightBackground,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append("agrega una cuenta")
        }
        withStyle(SpanStyle(color = colors.onSurfaceVariant)) {
            append(" para empezar")
        }
    }

    Text(
        text = annotatedText,
        style = typography.bodyMedium,
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onNavigateToAccounts
            )
    )
}
