package com.personal.financez.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.financez.data.currencies.Currency
import com.personal.financez.data.currencies.CurrencyLoader
import com.personal.financez.data.currencies.getFlagForCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencySelectionScreen(
    initialCurrency: String = "COP",
    onConfirm: (String) -> Unit
) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme

    // Moneda seleccionada persistible de forma segura (String es compatible nativo con SaveableStateRegistry)
    var selectedCurrencyCode by rememberSaveable { mutableStateOf(initialCurrency) }
    var selectedCurrencyName by rememberSaveable {
        mutableStateOf(
            CurrencyLoader.defaultCurrencies.firstOrNull { it.code == initialCurrency }?.name ?: "Moneda principal"
        )
    }

    val selectedCurrency = remember(selectedCurrencyCode, selectedCurrencyName) {
        Currency(
            code = selectedCurrencyCode,
            name = selectedCurrencyName,
            flagEmoji = getFlagForCurrency(selectedCurrencyCode)
        )
    }

    // Control del menú desplegable rápido
    var isDropdownExpanded by remember { mutableStateOf(false) }

    // Control de la hoja completa con las 178 monedas
    var showFullCatalogSheet by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        containerColor = colors.background,
        bottomBar = {
            Surface(
                color = colors.background,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                ) {
                    Button(
                        onClick = { onConfirm(selectedCurrency.code) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.primary,
                            contentColor = colors.onPrimary
                        )
                    ) {
                        Text(
                            text = "Confirmar moneda base (${selectedCurrency.code})",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // 1. Saludo cálido y mensaje guía
            Text(
                text = "Bienvenido a FinanceZ",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colors.onBackground,
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Define la divisa principal de tus finanzas. Todos tus saldos, cuentas y reportes se calcularán a partir de esta moneda.",
                style = MaterialTheme.typography.bodyLarge,
                color = colors.onSurfaceVariant,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Etiqueta de sección
            Text(
                text = "MONEDA BASE",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.2.sp,
                color = colors.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Selector Desplegable Compacto y Elegante
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(colors.surface)
                        .border(1.dp, colors.outline.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .clickable { isDropdownExpanded = true }
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedCurrency.flagEmoji,
                        fontSize = 28.sp
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = selectedCurrency.code,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurface
                        )
                        Text(
                            text = selectedCurrency.name,
                            fontSize = 14.sp,
                            color = colors.onSurfaceVariant
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Desplegar",
                        tint = colors.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Menú desplegable con las divisas frecuentes
                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false },
                    modifier = Modifier
                        .background(colors.surface)
                        .border(1.dp, colors.outline.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                ) {
                    CurrencyLoader.defaultCurrencies.forEach { currency ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = currency.flagEmoji, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = "${currency.code} — ${currency.name}",
                                        color = if (currency.code == selectedCurrency.code) colors.primary else colors.onSurface,
                                        fontWeight = if (currency.code == selectedCurrency.code) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            },
                            onClick = {
                                selectedCurrencyCode = currency.code
                                selectedCurrencyName = currency.name
                                isDropdownExpanded = false
                            }
                        )
                    }

                    HorizontalDivider(
                        color = colors.outline.copy(alpha = 0.3f),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    // Opción para abrir el catálogo completo de 178 monedas
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🌐", fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Buscar otra divisa...",
                                    color = colors.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        },
                        onClick = {
                            isDropdownExpanded = false
                            showFullCatalogSheet = true
                        }
                    )
                }
            }
        }
    }

    // 3. Modal Bottom Sheet (Carga diferida solo al tocar "Buscar otra divisa...")
    if (showFullCatalogSheet) {
        val allCurrencies = remember { CurrencyLoader.loadAllCurrencies(context) }
        var searchQuery by rememberSaveable { mutableStateOf("") }

        val filteredCurrencies = remember(searchQuery, allCurrencies) {
            allCurrencies.filter { it.matchesQuery(searchQuery) }
        }

        ModalBottomSheet(
            onDismissRequest = { showFullCatalogSheet = false },
            sheetState = sheetState,
            containerColor = colors.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f)
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Seleccionar divisa",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Buscador sin bordes genéricos
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(text = "Buscar código (ej. JPY)", color = colors.onSurfaceVariant)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = colors.onSurfaceVariant
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colors.onSurface,
                        unfocusedTextColor = colors.onSurface,
                        focusedContainerColor = colors.background,
                        unfocusedContainerColor = colors.background,
                        focusedBorderColor = colors.primary,
                        unfocusedBorderColor = colors.outline.copy(alpha = 0.5f),
                        cursorColor = colors.primary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Lista de las 178 monedas sin cuadritos genéricos
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(filteredCurrencies, key = { it.code }) { currency ->
                        val isSelected = currency.code == selectedCurrency.code

                        CurrencyListItem(
                            currency = currency,
                            isSelected = isSelected,
                            onClick = {
                                selectedCurrencyCode = currency.code
                                selectedCurrencyName = currency.name
                                showFullCatalogSheet = false
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Componente modular de fila de divisa: Bandera, código audaz y nombre completo.
 * Sin cajas genéricas alrededor de las siglas.
 */
@Composable
fun CurrencyListItem(
    currency: Currency,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) colors.primary.copy(alpha = 0.12f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Bandera emoji amplia y limpia
        Text(
            text = currency.flagEmoji,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Código tipográfico audaz
        Text(
            text = currency.code,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = if (isSelected) colors.primary else colors.onSurface
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Nombre de la divisa alineado con balance visual
        Text(
            text = currency.name,
            color = colors.onSurfaceVariant,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )

        // Indicador discreto de selección
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(colors.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Seleccionada",
                    tint = colors.onPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
