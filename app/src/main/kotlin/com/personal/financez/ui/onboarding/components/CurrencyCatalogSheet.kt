package com.personal.financez.ui.onboarding.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.financez.data.currencies.Currency
import com.personal.financez.data.currencies.CurrencyLoader
import com.personal.financez.ui.theme.CreatoDisplay

/**
 * Modal BottomSheet para explorar y filtrar el catálogo completo de divisas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyCatalogSheet(
    selectedCurrencyCode: String,
    onDismiss: () -> Unit,
    onSelectCurrency: (Currency) -> Unit,
    sheetState: SheetState
) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val allCurrencies = remember { CurrencyLoader.loadAllCurrencies(context) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isSearchFocused by remember { mutableStateOf(false) }

    val filteredCurrencies = remember(searchQuery, allCurrencies) {
        allCurrencies.filter { it.matchesQuery(searchQuery) }
    }

    val sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = sheetShape,
        containerColor = colors.surface,
        tonalElevation = 0.dp,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.90f)
                .drawWithContent {
                    drawContent()
                    val strokeWidth = 1.dp.toPx()
                    val halfStroke = strokeWidth / 2f
                    val cornerRadius = 24.dp.toPx()
                    val highlightColor = colors.outline.copy(alpha = 0.5f)
                    val path = Path().apply {
                        moveTo(halfStroke, cornerRadius)
                        arcTo(
                            rect = Rect(halfStroke, halfStroke, cornerRadius * 2 - halfStroke, cornerRadius * 2 - halfStroke),
                            startAngleDegrees = 180f,
                            sweepAngleDegrees = 90f,
                            forceMoveTo = false
                        )
                        lineTo(size.width - cornerRadius, halfStroke)
                        arcTo(
                            rect = Rect(size.width - cornerRadius * 2 + halfStroke, halfStroke, size.width - halfStroke, cornerRadius * 2 - halfStroke),
                            startAngleDegrees = 270f,
                            sweepAngleDegrees = 90f,
                            forceMoveTo = false
                        )
                    }
                    drawPath(path, color = highlightColor, style = Stroke(width = strokeWidth))
                }
                .padding(horizontal = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 12.dp, bottom = 12.dp)
                    .size(width = 36.dp, height = 4.dp)
                    .clip(CircleShape)
                    .background(colors.outline.copy(alpha = 0.5f))
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Seleccionar divisa",
                        style = typography.headlineMedium,
                        color = colors.onBackground
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (filteredCurrencies.size == 1) {
                            "1 divisa disponible"
                        } else {
                            "${filteredCurrencies.size} divisas disponibles"
                        },
                        style = typography.bodyMedium,
                        color = colors.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(colors.background)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isSearchFocused = it.isFocused },
                textStyle = typography.bodyLarge.copy(
                    fontFamily = CreatoDisplay,
                    fontWeight = FontWeight.Normal,
                    color = colors.onSurface
                ),
                placeholder = if (!isSearchFocused && searchQuery.isEmpty()) {
                    {
                        Text(
                            text = "Buscar por moneda o código...",
                            fontFamily = CreatoDisplay,
                            fontWeight = FontWeight.Normal,
                            fontSize = 15.sp,
                            color = colors.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                } else null,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = if (isSearchFocused) colors.primary else colors.onSurfaceVariant,
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
                    focusedContainerColor = colors.background,
                    unfocusedContainerColor = colors.background,
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
                            style = typography.displayMedium,
                            color = colors.onSurfaceVariant.copy(alpha = 0.4f)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Sin resultados para \"$searchQuery\"",
                            style = typography.bodyLarge,
                            color = colors.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredCurrencies, key = { it.code }) { currency ->
                        val isSelected = currency.code == selectedCurrencyCode

                        TactileCurrencyItem(
                            currency = currency,
                            isSelected = isSelected,
                            onSelect = { onSelectCurrency(currency) }
                        )
                    }
                }
            }
        }
    }
}
