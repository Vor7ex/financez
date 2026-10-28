package com.personal.financez.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val FinanceZColorScheme = darkColorScheme(
    primary = OceanMist,
    onPrimary = DarkKhakiDeep,
    secondary = HunterGreen,
    onSecondary = FrostedMint,
    background = DarkKhakiDeep,
    onBackground = FrostedMint,
    surface = DarkKhakiMedium,
    onSurface = FrostedMint,
    onSurfaceVariant = FrostedMint.copy(alpha = 0.65f),
    outline = HunterGreen
)

@Composable
fun FinanceZTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FinanceZColorScheme,
        content = content
    )
}
