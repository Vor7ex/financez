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

// Familia secundaria oficial: Creato Display
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

// Tipografía semántica de FinanceZ basada en Material 3:
// - Creato Display para Brand, Display, Headlines, Títulos y Labels
// - Coolvetica para cuerpos de texto editoriales y descriptivos
val FinanceZTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 68.sp,
        lineHeight = 72.sp,
        letterSpacing = (-0.5).sp
    ),
    displayMedium = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 52.sp,
        lineHeight = 58.sp,
        letterSpacing = (-0.25).sp
    ),
    displaySmall = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 42.sp,
        lineHeight = 48.sp,
        letterSpacing = 0.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 38.sp,
        lineHeight = 44.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.SemiBold,
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
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 1.5.sp
    ),
    labelMedium = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 1.2.sp
    ),
    labelSmall = TextStyle(
        fontFamily = CreatoDisplay,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.4.sp
    )
)
