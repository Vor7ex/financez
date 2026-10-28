package com.personal.financez.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val FinanceZColorScheme = darkColorScheme(
    primary = VibrantMint,
    onPrimary = DeepOnyxText,
    secondary = DarkHunter,
    onSecondary = FrostedIceMint,
    background = ObsidianForest,
    onBackground = FrostedIceMint,
    surface = DeepKhakiShadow,
    onSurface = FrostedIceMint,
    onSurfaceVariant = MutedSage,
    outline = DarkHunter
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
