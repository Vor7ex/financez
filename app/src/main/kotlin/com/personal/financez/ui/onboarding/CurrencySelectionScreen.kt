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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.financez.data.currencies.Currency
import com.personal.financez.data.currencies.CurrencyLoader

@Composable
fun CurrencySelectionScreen(
    initialCurrency: String = "COP",
    onConfirm: (String) -> Unit
) {
    val context = LocalContext.current
    val allCurrencies = remember { CurrencyLoader.loadFromAssets(context) }

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedCurrencyCode by rememberSaveable { mutableStateOf(initialCurrency) }

    val filteredCurrencies = remember(searchQuery, allCurrencies) {
        if (searchQuery.isBlank()) {
            allCurrencies
        } else {
            val query = searchQuery.trim().lowercase()
            allCurrencies.filter {
                it.code.lowercase().contains(query) || it.name.lowercase().contains(query)
            }
        }
    }

    val colors = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colors.background,
        bottomBar = {
            Surface(
                color = colors.background,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Button(
                        onClick = { onConfirm(selectedCurrencyCode) },
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
                            text = "Confirmar moneda base ($selectedCurrencyCode)",
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
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Buscador para filtrar rápidamente entre las 178 opciones
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Buscar moneda o código...", color = colors.onSurfaceVariant)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = colors.onSurfaceVariant
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = colors.onSurface,
                    unfocusedTextColor = colors.onSurface,
                    focusedContainerColor = colors.surface,
                    unfocusedContainerColor = colors.surface,
                    focusedBorderColor = colors.primary,
                    unfocusedBorderColor = Color.Transparent,
                    cursorColor = colors.primary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Lista deslizable de monedas
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredCurrencies, key = { it.code }) { currency ->
                    val isSelected = currency.code == selectedCurrencyCode

                    CurrencyRow(
                        currency = currency,
                        isSelected = isSelected,
                        onClick = { selectedCurrencyCode = currency.code }
                    )
                }
            }
        }
    }
}

@Composable
private fun CurrencyRow(
    currency: Currency,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val borderColor = if (isSelected) colors.primary else Color.Transparent
    val backgroundColor = if (isSelected) colors.surface.copy(alpha = 0.95f) else colors.surface

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(backgroundColor)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Código de la moneda en píldora visual
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) colors.primary.copy(alpha = 0.2f) else colors.secondary.copy(alpha = 0.4f))
                .padding(horizontal = 10.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = currency.code,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = if (isSelected) colors.primary else colors.onSecondary
            )
        }

        Spacer(modifier = Modifier.size(14.dp))

        // Nombre de la divisa
        Text(
            text = currency.name,
            color = colors.onSurface,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f)
        )

        // Indicador de selección
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
