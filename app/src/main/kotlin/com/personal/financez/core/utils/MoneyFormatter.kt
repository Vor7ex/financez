package com.personal.financez.core.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs

/**
 * Formatea un valor monetario almacenado en centavos (`Long`) a una representación legible
 * con separadores de miles y símbolo de moneda.
 */
fun Long.formatMoney(currencyCode: String = "COP", showDecimalsAlways: Boolean = false): String {
    val isNegative = this < 0
    val absValue = abs(this)
    val integerPart = absValue / 100
    val decimalPart = absValue % 100

    val symbols = DecimalFormatSymbols(Locale("es", "CO")).apply {
        groupingSeparator = '.'
        decimalSeparator = ','
    }
    val formatter = DecimalFormat("#,##0", symbols)
    val formattedInteger = formatter.format(integerPart)

    val sign = if (isNegative) "-" else ""
    return if (showDecimalsAlways || decimalPart > 0) {
        val formattedDecimals = decimalPart.toString().padStart(2, '0')
        "$sign$ $formattedInteger,$formattedDecimals $currencyCode"
    } else {
        "$sign$ $formattedInteger $currencyCode"
    }
}
