package com.personal.financez.ui.theme

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ThemeTypographyTest {

    @Test
    fun financez_typography_configures_coolvetica_for_display_and_labels() {
        val typography = FinanceZTypography

        assertNotNull("FinanceZTypography debe estar inicializado", typography)
        assertEquals(
            "headlineLarge debe usar Coolvetica",
            Coolvetica,
            typography.headlineLarge.fontFamily
        )
        assertEquals(
            "labelLarge debe usar CoolveticaHeavy para botones de máximo impacto",
            CoolveticaHeavy,
            typography.labelLarge.fontFamily
        )
    }
}
