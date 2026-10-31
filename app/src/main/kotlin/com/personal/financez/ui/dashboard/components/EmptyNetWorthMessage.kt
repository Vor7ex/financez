package com.personal.financez.ui.dashboard.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

/**
 * Mensaje informativo cuando el patrimonio está en 0, con enlace interactivo subrayado
 * para desplazarse fluidamente a la pestaña de cuentas.
 */
@Composable
fun EmptyNetWorthMessage(
    onNavigateToAccounts: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val annotatedText = buildAnnotatedString {
        withStyle(SpanStyle(color = colors.onSurfaceVariant)) {
            append("Aún no tienes patrimonio registrado, ")
        }
        withStyle(
            SpanStyle(
                color = colors.primary,
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
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onNavigateToAccounts()
            }
    )
}
