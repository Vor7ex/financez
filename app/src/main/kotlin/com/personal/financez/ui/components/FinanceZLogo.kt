package com.personal.financez.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.personal.financez.ui.theme.CreatoDisplay

/**
 * Logotipo oficial de FinanceZ con acento tipográfico en las últimas dos letras "EZ".
 */
@Composable
fun FinanceZLogo(
    modifier: Modifier = Modifier,
    baseFontSize: TextUnit = 18.sp,
    accentFontSize: TextUnit = 24.sp
) {
    val colors = MaterialTheme.colorScheme

    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    fontFamily = CreatoDisplay,
                    fontWeight = FontWeight.Black,
                    fontSize = baseFontSize,
                    letterSpacing = 2.sp,
                    color = colors.onBackground
                )
            ) {
                append("FINANC")
            }
            withStyle(
                SpanStyle(
                    fontFamily = CreatoDisplay,
                    fontWeight = FontWeight.Black,
                    fontSize = accentFontSize,
                    letterSpacing = 2.sp,
                    color = colors.primary
                )
            ) {
                append("EZ")
            }
        }
    )
}
