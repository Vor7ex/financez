package com.personal.financez.ui.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
    val scrollState = rememberScrollState()

    var selectedCode by rememberSaveable { mutableStateOf(initialCurrency) }
    var selectedName by rememberSaveable {
        mutableStateOf(
            CurrencyLoader.defaultCurrencies.firstOrNull { it.code == initialCurrency }?.name
                ?: "Peso colombiano"
        )
    }

    val selectedCurrency = remember(selectedCode, selectedName) {
        Currency(
            code = selectedCode,
            name = selectedName,
            flagEmoji = getFlagForCurrency(selectedCode)
        )
    }

    var showFullCatalogSheet by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        containerColor = colors.background,
        bottomBar = {
            BottomConfirmationBar(
                onConfirm = { onConfirm(selectedCurrency.code) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // 1. Cabecera editorial limpia (sin "PASO 01")
            BrandHeader()

            Spacer(modifier = Modifier.height(28.dp))

            // 2. Tarjeta Hero con la divisa actualmente seleccionada (sin metadatos técnicos)
            SelectedCurrencyCard(currency = selectedCurrency)

            Spacer(modifier = Modifier.height(28.dp))

            // 3. Selección rápida de monedas frecuentes (sin inscripción "ISO 4217")
            QuickSelectionSection(
                selectedCode = selectedCurrency.code,
                onSelectCurrency = { currency ->
                    selectedCode = currency.code
                    selectedName = currency.name
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 4. Barra de búsqueda reordenada DESPUÉS de selección rápida (sin "catálogo")
            SearchTriggerField(
                onClick = { showFullCatalogSheet = true }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Modal BottomSheet con el catálogo completo de divisas
    if (showFullCatalogSheet) {
        CurrencyCatalogSheet(
            selectedCurrencyCode = selectedCurrency.code,
            onDismiss = { showFullCatalogSheet = false },
            onSelectCurrency = { currency ->
                selectedCode = currency.code
                selectedName = currency.name
                showFullCatalogSheet = false
            },
            sheetState = sheetState
        )
    }
}

/**
 * Cabecera editorial con la marca y el mensaje de bienvenida.
 */
@Composable
fun BrandHeader(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme

    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(colors.primary)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "FINANCEZ",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp,
                color = colors.onBackground
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Bienvenido",
            fontSize = 34.sp,
            fontWeight = FontWeight.ExtraBold,
            color = colors.onBackground,
            letterSpacing = (-1).sp,
            lineHeight = 38.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Elige la moneda base de tus finanzas. Todas tus cuentas, métricas y balances se consolidarán en esta divisa.",
            fontSize = 14.sp,
            color = colors.onSurfaceVariant,
            lineHeight = 21.sp
        )
    }
}

/**
 * Tarjeta que destaca la moneda seleccionada de manera limpia y sin ruidos técnicos.
 */
@Composable
fun SelectedCurrencyCard(
    currency: Currency,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        colors.surface,
                        colors.primary.copy(alpha = 0.06f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = colors.primary.copy(alpha = 0.35f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(22.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AnimatedContent(
                targetState = currency,
                transitionSpec = {
                    (slideInVertically(animationSpec = tween(220)) { height -> height / 2 } +
                        fadeIn(animationSpec = tween(220)))
                        .togetherWith(
                            slideOutVertically(animationSpec = tween(180)) { height -> -height / 2 } +
                                fadeOut(animationSpec = tween(150))
                        )
                },
                label = "SelectedCurrencyTransition"
            ) { targetCurrency ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = targetCurrency.code,
                                fontSize = 36.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = (-1).sp,
                                color = colors.onSurface
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = targetCurrency.flagEmoji,
                                fontSize = 26.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = targetCurrency.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = colors.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            HorizontalDivider(
                color = colors.outline.copy(alpha = 0.15f),
                thickness = 0.5.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Formato de balance",
                    fontSize = 12.sp,
                    color = colors.onSurfaceVariant
                )
                Text(
                    text = "${currency.code} 1,250,000.00",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.onSurface
                )
            }
        }
    }
}

/**
 * Sección de selección rápida con las 5 divisas más frecuentes.
 */
@Composable
fun QuickSelectionSection(
    selectedCode: String,
    onSelectCurrency: (Currency) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "SELECCIÓN RÁPIDA",
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.4.sp,
            color = colors.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CurrencyLoader.defaultCurrencies.forEach { currency ->
                val isSelected = currency.code == selectedCode

                TactileCurrencyItem(
                    currency = currency,
                    isSelected = isSelected,
                    onSelect = { onSelectCurrency(currency) }
                )
            }
        }
    }
}

/**
 * Barra disparadora de búsqueda ubicada después de la selección rápida.
 */
@Composable
fun SearchTriggerField(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(colors.surface)
            .border(1.dp, colors.outline.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = colors.primary,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "Buscar entre más de 50 divisas...",
            fontSize = 14.sp,
            color = colors.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Item táctil individual para listas de divisas.
 */
@Composable
fun TactileCurrencyItem(
    currency: Currency,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    val containerColor by animateColorAsState(
        targetValue = if (isSelected) colors.primary.copy(alpha = 0.08f) else colors.surface,
        animationSpec = tween(200),
        label = "itemBg"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) colors.primary else colors.outline.copy(alpha = 0.18f),
        animationSpec = tween(200),
        label = "itemBorder"
    )

    val borderWidth by animateDpAsState(
        targetValue = if (isSelected) 1.5.dp else 1.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "itemBorderWidth"
    )

    val checkScale by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.85f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "checkScale"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(containerColor)
            .border(borderWidth, borderColor, RoundedCornerShape(14.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onSelect
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(colors.background)
                .border(
                    width = 0.5.dp,
                    color = if (isSelected) colors.primary.copy(alpha = 0.4f) else colors.outline.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = currency.flagEmoji,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = currency.code,
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) colors.primary else colors.onSurface
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = currency.name,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) colors.onSurface else colors.onSurfaceVariant,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Box(
            modifier = Modifier
                .size(22.dp)
                .scale(checkScale)
                .clip(CircleShape)
                .background(if (isSelected) colors.primary else Color.Transparent)
                .border(
                    width = 1.5.dp,
                    color = if (isSelected) colors.primary else colors.outline.copy(alpha = 0.35f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Seleccionada",
                    tint = colors.onPrimary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

/**
 * Fila de visualización dentro del catálogo modal.
 */
@Composable
fun CatalogCurrencyRow(
    currency: Currency,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) colors.primary.copy(alpha = 0.10f) else colors.surface)
            .border(
                width = if (isSelected) 1.dp else 0.5.dp,
                color = if (isSelected) colors.primary.copy(alpha = 0.5f) else colors.outline.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = currency.flagEmoji,
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = currency.name,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = colors.onSurface,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(if (isSelected) colors.primary else colors.background)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = currency.code,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = if (isSelected) colors.onPrimary else colors.onSurfaceVariant
            )
        }
    }
}

/**
 * Modal BottomSheet para explorar y filtrar las divisas disponibles.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyCatalogSheet(
    selectedCurrencyCode: String,
    onDismiss: () -> Unit,
    onSelectCurrency: (Currency) -> Unit,
    sheetState: androidx.compose.material3.SheetState
) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme
    val allCurrencies = remember { CurrencyLoader.loadAllCurrencies(context) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    val filteredCurrencies = remember(searchQuery, allCurrencies) {
        allCurrencies.filter { it.matchesQuery(searchQuery) }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.background,
        tonalElevation = 0.dp,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 14.dp, bottom = 10.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .clip(CircleShape)
                    .background(colors.outline.copy(alpha = 0.4f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.90f)
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Seleccionar divisa",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp,
                        color = colors.onBackground
                    )
                    Text(
                        text = "${filteredCurrencies.size} divisas disponibles",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = colors.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(colors.surface)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = colors.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Buscar por moneda o código (ej. CLP)...",
                        color = colors.onSurfaceVariant.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = colors.primary,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Borrar búsqueda",
                                tint = colors.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = colors.onSurface,
                    unfocusedTextColor = colors.onSurface,
                    focusedContainerColor = colors.surface,
                    unfocusedContainerColor = colors.surface,
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = colors.outline.copy(alpha = 0.25f),
                    cursorColor = colors.primary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredCurrencies.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "404",
                            fontSize = 28.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurfaceVariant.copy(alpha = 0.4f)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Sin resultados para \"$searchQuery\"",
                            fontSize = 14.sp,
                            color = colors.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(filteredCurrencies, key = { it.code }) { currency ->
                        val isSelected = currency.code == selectedCurrencyCode

                        CatalogCurrencyRow(
                            currency = currency,
                            isSelected = isSelected,
                            onClick = { onSelectCurrency(currency) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Botón estático y reutilizable de confirmación.
 * Fuente: Monospace, peso bold, letter spacing amplio, sin flechas ni textos dinámicos.
 */
@Composable
fun ConfirmButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.primary,
            contentColor = colors.onPrimary
        )
    ) {
        Text(
            text = "CONFIRMAR",
            fontSize = 15.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp
        )
    }
}

/**
 * Barra de navegación inferior que contiene el ConfirmButton reutilizable.
 */
@Composable
fun BottomConfirmationBar(
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        color = colors.background,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            HorizontalDivider(
                color = colors.outline.copy(alpha = 0.15f),
                thickness = 0.5.dp
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                ConfirmButton(onClick = onConfirm)
            }
        }
    }
}
