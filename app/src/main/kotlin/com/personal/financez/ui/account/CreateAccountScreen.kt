package com.personal.financez.ui.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.financez.data.local.AccountType
import com.personal.financez.ui.account.components.FormItem
import com.personal.financez.ui.components.PillSelector
import com.personal.financez.ui.components.TextField
import com.personal.financez.ui.components.TopBar

@Composable
fun CreateAccountScreen(
    state: CreateAccountUiState,
    onNameChange: (String) -> Unit,
    onTypeChange: (AccountType) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // Barra superior reutilizable
        TopBar(
            title = "Nueva Cuenta",
            onNavigateBack = onNavigateBack
        )

        // Contenido del formulario modularizado con FormItem
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Elemento: Nombre de la cuenta
            FormItem(title = "Nombre de la cuenta") {
                TextField(
                    value = state.name,
                    onValueChange = onNameChange,
                    placeholderText = if (state.type == AccountType.EFECTIVO) "Ej. Efectivo cartera" else "Ej. Ahorros Bancolombia",
                    errorMessage = state.nameError
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Elemento: Tipo de cuenta
            FormItem(title = "Tipo de cuenta") {
                val accountTypes = remember {
                    listOf(
                        AccountType.CUENTA_BANCARIA to "Bancaria",
                        AccountType.EFECTIVO to "Efectivo"
                    )
                }
                val selectedTypeIndex = if (state.type == AccountType.CUENTA_BANCARIA) 0 else 1

                PillSelector(
                    items = accountTypes,
                    selectedIndex = selectedTypeIndex,
                    onSelect = { (type, _) -> onTypeChange(type) },
                    labelProvider = { (_, label) -> label }
                )
            }
        }
    }
}
