package com.personal.financez.ui.onboarding.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.financez.data.currencies.Currency

/**
 * Sección de selección rápida con divisas frecuentes y la moneda secundaria seleccionada si aplica.
 */
@Composable
fun QuickSelectionSection(
    currencies: List<Currency>,
    selectedCode: String,
    onSelectCurrency: (Currency) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        currencies.forEach { currency ->
            val isSelected = currency.code == selectedCode

            TactileCurrencyItem(
                currency = currency,
                isSelected = isSelected,
                onSelect = { onSelectCurrency(currency) }
            )
        }
    }
}
