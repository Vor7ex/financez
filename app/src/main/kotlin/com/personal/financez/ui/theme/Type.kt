package com.personal.financez.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.personal.financez.R

// Familia de marca oficial: Coolvetica
val Coolvetica = FontFamily(
    Font(R.font.coolvetica_rg, FontWeight.Normal),
    Font(R.font.coolvetica_rg_it, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.coolvetica_rg_cond, FontWeight.Medium),
    Font(R.font.coolvetica_rg_cram, FontWeight.SemiBold),
    Font(R.font.coolvetica_hv_comp, FontWeight.Black)
)

// Acceso directo a Coolvetica Heavy Compressed para botones y logotipos de máximo impacto
val CoolveticaHeavy = FontFamily(
    Font(R.font.coolvetica_hv_comp, FontWeight.Black)
)

// Tipografía semántica de FinanceZ basada en Material 3 con escala visual amplia para Coolvetica
val FinanceZTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = Coolvetica,
        fontWeight = FontWeight.SemiBold,
        fontSize = 68.sp,
        lineHeight = 72.sp,
        letterSpacing = (-0.5).sp
    ),
    displayMedium = TextStyle(
        fontFamily = Coolvetica,
        fontWeight = FontWeight.SemiBold,
        fontSize = 52.sp,
        lineHeight = 58.sp,
        letterSpacing = (-0.25).sp
    ),
    displaySmall = TextStyle(
        fontFamily = Coolvetica,
        fontWeight = FontWeight.Normal,
        fontSize = 42.sp,
        lineHeight = 48.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = Coolvetica,
        fontWeight = FontWeight.SemiBold,
        fontSize = 40.sp,
        lineHeight = 46.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Coolvetica,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Coolvetica,
        fontWeight = FontWeight.Medium,
        fontSize = 26.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Coolvetica,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Coolvetica,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = Coolvetica,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Coolvetica,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.25.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Coolvetica,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Coolvetica,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.4.sp
    ),
    labelLarge = TextStyle(
        fontFamily = CoolveticaHeavy,
        fontWeight = FontWeight.Black,
        fontSize = 50.sp,
        lineHeight = 50.sp,
        letterSpacing = 5.2.sp
    ),
    labelMedium = TextStyle(
        fontFamily = CoolveticaHeavy,
        fontWeight = FontWeight.Black,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 1.2.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Coolvetica,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 1.4.sp
    )
)
