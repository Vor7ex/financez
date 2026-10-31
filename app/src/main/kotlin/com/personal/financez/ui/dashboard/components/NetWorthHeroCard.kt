package com.personal.financez.ui.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.personal.financez.core.utils.formatMoney

/**
 * Tarjeta Hero de Patrimonio que muestra el saldo total consolidado
 * con curvaturas de 14.dp y tipografía oficial Creato Display.
 */
@Composable
fun NetWorthHeroCard(
    consolidatedNetWorth: Long,
    baseCurrency: String,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(colors.surface)
            .border(
                width = 1.dp,
                color = colors.outline.copy(alpha = 0.25f),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 24.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Patrimonio total",
            style = typography.titleSmall,
            color = colors.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = consolidatedNetWorth.formatMoney(baseCurrency),
            style = typography.displayMedium,
            color = colors.onBackground
        )
    }
}
