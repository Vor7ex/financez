package com.personal.financez.ui.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.financez.ui.dashboard.DashboardTab

/**
 * Barra superior del Dashboard que contiene el selector de pestañas (Patrimonio / Cuentas)
 * y el botón de engranaje de configuración alineado simétricamente a la derecha.
 */
@Composable
fun DashboardTopBar(
    selectedTab: DashboardTab,
    onTabSelected: (DashboardTab) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    pagerState: PagerState? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        DashboardTabSelector(
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            pagerState = pagerState,
            modifier = Modifier.weight(1f)
        )

        SettingsGearButton(
            onClick = onSettingsClick
        )
    }
}
