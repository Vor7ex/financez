package com.personal.financez.ui.dashboard.components

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.personal.financez.ui.components.PillSelector
import com.personal.financez.ui.dashboard.DashboardTab

/**
 * Selector de pestañas tipo cápsula (stadium) para alternar entre "Patrimonio" y "Cuentas".
 * Construido sobre el componente reutilizable PillSelector.
 */
@Composable
fun DashboardTabSelector(
    selectedTab: DashboardTab,
    onTabSelected: (DashboardTab) -> Unit,
    modifier: Modifier = Modifier,
    pagerState: PagerState? = null
) {
    val tabs = remember {
        listOf(
            DashboardTab.PATRIMONIO to "Patrimonio",
            DashboardTab.CUENTAS to "Cuentas"
        )
    }

    val selectedIndex = if (selectedTab == DashboardTab.PATRIMONIO) 0 else 1

    val overrideBias by remember(pagerState) {
        derivedStateOf {
            if (pagerState != null) {
                val rawOffset = pagerState.currentPage + pagerState.currentPageOffsetFraction
                (rawOffset * 2f - 1f).coerceIn(-1f, 1f)
            } else {
                null
            }
        }
    }

    PillSelector(
        items = tabs,
        selectedIndex = selectedIndex,
        onSelect = { (tab, _) -> onTabSelected(tab) },
        labelProvider = { (_, label) -> label },
        overrideHorizontalBias = overrideBias,
        modifier = modifier
    )
}
