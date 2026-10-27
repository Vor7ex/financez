package com.personal.financez.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val FinanceZColorScheme = darkColorScheme(
    primary = MutedTeal,
    onPrimary = DarkCoffee,
    primaryContainer = DarkCoffee,
    onPrimaryContainer = LightBronze,
    secondary = LightBronze,
    onSecondary = DarkCoffee,
    error = ScarletRush,
    background = BackgroundDark,
    onBackground = TextWhite,
    surface = SurfaceCardDark,
    onSurface = TextWhite
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
