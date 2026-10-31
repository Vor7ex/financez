package com.personal.financez.core.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class MoneyFormatterTest {

    @Test
    fun formats_cop_amounts_with_thousands_separator_and_no_unnecessary_decimals() {
        assertEquals("$ 1.500.000 COP", 150_000_000L.formatMoney("COP"))
        assertEquals("$ 0 COP", 0L.formatMoney("COP"))
        assertEquals("-$ 25.000 COP", (-25_000_00L).formatMoney("COP"))
    }

    @Test
    fun formats_foreign_currencies_with_decimals_when_cents_present() {
        assertEquals("$ 5.000,50 USD", 500_050L.formatMoney("USD"))
        assertEquals("$ 100 USD", 100_00L.formatMoney("USD"))
    }
}
