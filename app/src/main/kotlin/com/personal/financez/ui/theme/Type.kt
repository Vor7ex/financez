package com.personal.financez.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.personal.financez.R

// Familia tipográfica oficial de FinanceZ: Creato Display
val CreatoDisplay = FontFamily(
    Font(R.font.creato_display_thin, FontWeight.Thin),
    Font(R.font.creato_display_thin_italic, FontWeight.Thin, FontStyle.Italic),
    Font(R.font.creato_display_light, FontWeight.Light),
    Font(R.font.creato_display_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.creato_display_regular, FontWeight.Normal),
    Font(R.font.creato_display_regular_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.creato_display_medium, FontWeight.Medium),
    Font(R.font.creato_display_medium_italic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.creato_display_bold, FontWeight.Bold),
    Font(R.font.creato_display_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.creato_display_extra_bold, FontWeight.ExtraBold),
    Font(R.font.creato_display_extra_bold_italic, FontWeight.ExtraBold, FontStyle.Italic),
    Font(R.font.creato_display_black, FontWeight.Black),
    Font(R.font.creato_display_black_italic, FontWeight.Black, FontStyle.Italic)
)

/**
 * Escala tipográfica semántica de FinanceZ basada en Material 3.
 * Unificada 100% en Creato Display para garantizar coherencia visual,
 * sobriedad de neobanco y máxima legibilidad en interfaces financieras.
 */
val FinanceZTypography = Typography(
    // 1. DISPLAY — Grandes balances patrimoniales y códigos ISO principales
    displayLarge = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 56.sp,
        lineHeight = 62.sp,
        letterSpacing = (-0.5).sp
    ),
    displayMedium = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        lineHeight = 54.sp,
        letterSpacing = (-0.25).sp
    ),
    displaySmall = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.SemiBold,
        fontSize = 38.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),

    // 2. HEADLINE — Títulos de pantalla, cabeceras editoriales y modales
    headlineLarge = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),

    // 3. TITLE — Nombres de cuentas, divisas y encabezados de listas
    titleLarge = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // 4. BODY — Cuerpos de texto, explicaciones, inputs de búsqueda y subtítulos
    bodyLarge = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),

    // 5. LABEL — Botones de acción, badges, pastillas monetarias y metadatos
    labelLarge = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 20.sp,
        letterSpacing = 1.5.sp
    ),
    labelMedium = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 18.sp,
        letterSpacing = 1.0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)
