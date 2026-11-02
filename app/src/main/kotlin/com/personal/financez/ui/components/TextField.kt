package com.personal.financez.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Campo de texto reutilizable con la estética oficial de FinanceZ.
 */
@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String? = null,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    containerColor: Color? = null
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val resolvedContainerColor = containerColor ?: colors.surface

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        textStyle = typography.bodyLarge.copy(color = colors.onSurface),
        placeholder = placeholderText?.let {
            {
                Text(
                    text = it,
                    style = typography.bodyLarge,
                    color = colors.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = errorMessage != null,
        supportingText = errorMessage?.let {
            {
                Text(
                    text = it,
                    style = typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = colors.onSurface,
            unfocusedTextColor = colors.onSurface,
            focusedContainerColor = resolvedContainerColor,
            unfocusedContainerColor = resolvedContainerColor,
            focusedBorderColor = colors.primary,
            unfocusedBorderColor = colors.outline.copy(alpha = 0.35f),
            cursorColor = colors.primary
        )
    )
}
