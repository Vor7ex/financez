package com.personal.financez.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Botón estático y reutilizable de confirmación con color sólido institucional.
 */
@Composable
fun ConfirmButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "CONFIRMAR"
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.primary,
            contentColor = colors.onPrimary
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = text,
            style = typography.labelLarge.copy(
                fontSize = 16.sp,
                letterSpacing = 1.5.sp
            )
        )
    }
}

/**
 * Barra de navegación inferior con línea divisoria sólida que aloja el ConfirmButton.
 */
@Composable
fun BottomConfirmationBar(
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    buttonText: String = "CONFIRMAR"
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.background)
    ) {
        HorizontalDivider(
            color = colors.outline.copy(alpha = 0.35f),
            thickness = 1.dp
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 14.dp)
        ) {
            ConfirmButton(onClick = onConfirm, text = buttonText)
        }
    }
}
