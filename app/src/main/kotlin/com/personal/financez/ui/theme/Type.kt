package com.personal.financez.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
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
