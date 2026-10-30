package com.personal.financez.ui.onboarding

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.financez.data.currencies.Currency
import com.personal.financez.data.currencies.CurrencyLoader
import com.personal.financez.data.currencies.getFlagForCurrency
import com.personal.financez.ui.components.BottomConfirmationBar
import com.personal.financez.ui.components.FinanceZLogo
import com.personal.financez.ui.onboarding.components.BrandHeader
import com.personal.financez.ui.onboarding.components.CurrencyCatalogSheet
import com.personal.financez.ui.onboarding.components.QuickSelectionSection
import com.personal.financez.ui.onboarding.components.SearchTriggerField
import com.personal.financez.ui.onboarding.components.SelectedCurrencyCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CurrencySelectionScreen(
    initialCurrency: String = "COP",
    onConfirm: (String) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current
    val pullOffset = remember { Animatable(0f) }
    val maxPullDistance = 50.dp
    val density = LocalDensity.current
    val maxPullPx = with(density) { maxPullDistance.toPx() }

    var isLatched by remember { mutableStateOf(false) }
    var hasVibratedThisPull by remember { mutableStateOf(false) }
    var touchStartedWithLogoDeployed by remember { mutableStateOf(false) }

    val nestedScrollConnection = remember(maxPullPx) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // 1. Cuando el logo está desplegado y el usuario arrastra hacia arriba (para esconderlo)
                if (available.y < 0 && pullOffset.value > 0f) {
                    if (isLatched) {
                        isLatched = false
                    }

                    val newOffset = (pullOffset.value + available.y).coerceAtLeast(0f)
                    val consumedY = newOffset - pullOffset.value

                    coroutineScope.launch { pullOffset.snapTo(newOffset) }

                    // Si alcanzó 0 en este evento y fue un deslizamiento brusco/rápido (< -30f),
                    // consumimos todo el delta disponible para que la lista no salte antes de que onPreFling
                    // mate el momento residual.
                    if (newOffset == 0f && available.y < -30f) {
                        return Offset(0f, available.y)
                    }

                    return Offset(0f, consumedY)
                }

                // 2. Deslizamiento elástico hacia abajo en el tope superior
                if (available.y > 0 && scrollState.value == 0) {
                    if (pullOffset.value == 0f) {
                        hasVibratedThisPull = false
                    }

                    val friction = (1f - (pullOffset.value / (maxPullPx * 1.6f))).coerceIn(0.2f, 0.5f)
                    val newOffset = (pullOffset.value + available.y * friction).coerceAtMost(maxPullPx)

                    // ¿Alcanzó el límite de elasticidad (el techo)?
                    if (newOffset >= maxPullPx && !hasVibratedThisPull) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        hasVibratedThisPull = true
                        isLatched = true
                    }

                    coroutineScope.launch { pullOffset.snapTo(newOffset) }
                    return Offset(0f, available.y)
                }

                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                // Excepción: Cuando se despliega el logo y el usuario desliza para esconderlo:
                // Si hace un deslizamiento rápido y brusco hacia arriba (fling):
                // - El scroll se detiene de inmediato en el inicio de la página (scrollState = 0).
                // - Se elimina todo el momento residual (consumiendo available).
                // - Si el usuario quiere scrollear le tocará deslizar de nuevo.
                if (touchStartedWithLogoDeployed && available.y < -250f && scrollState.value < 40) {
                    isLatched = false
                    hasVibratedThisPull = false
                    if (scrollState.value > 0) {
                        scrollState.scrollTo(0)
                    }
                    coroutineScope.launch {
                        pullOffset.animateTo(0f, tween(durationMillis = 150, easing = FastOutSlowInEasing))
                    }
                    return available
                }

                if (isLatched) {
                    pullOffset.animateTo(maxPullPx, tween(durationMillis = 150, easing = FastOutSlowInEasing))
                } else if (pullOffset.value > 0f) {
                    hasVibratedThisPull = false
                    pullOffset.animateTo(0f, tween(durationMillis = 200, easing = FastOutSlowInEasing))
                }
                return Velocity.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                if (available.y > 150f && scrollState.value == 0) {
                    val target = (pullOffset.value + available.y * 0.04f)
                    if (target >= maxPullPx) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        hasVibratedThisPull = true
                        isLatched = true
                        pullOffset.animateTo(maxPullPx, tween(durationMillis = 180, easing = FastOutSlowInEasing))
                    } else {
                        hasVibratedThisPull = false
                        pullOffset.animateTo(0f, tween(durationMillis = 200, easing = FastOutSlowInEasing))
                    }
                    return Velocity(0f, available.y)
                } else if (isLatched) {
                    pullOffset.animateTo(maxPullPx, tween(durationMillis = 150, easing = FastOutSlowInEasing))
                } else if (pullOffset.value > 0f) {
                    hasVibratedThisPull = false
                    pullOffset.animateTo(0f, tween(durationMillis = 200, easing = FastOutSlowInEasing))
                }
                return Velocity.Zero
            }
        }
    }

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

    // Sexta divisa si se selecciona una que no pertenece a las 5 iniciales
    var extraCurrencyCode by rememberSaveable {
        mutableStateOf(
            if (CurrencyLoader.defaultCurrencies.none { it.code == initialCurrency }) initialCurrency else null
        )
    }
    var extraCurrencyName by rememberSaveable {
        mutableStateOf(
            if (CurrencyLoader.defaultCurrencies.none { it.code == initialCurrency }) selectedName else null
        )
    }

    val quickSelectionCurrencies = remember(extraCurrencyCode, extraCurrencyName) {
        val baseList = CurrencyLoader.defaultCurrencies
        if (extraCurrencyCode != null && extraCurrencyName != null && baseList.none { it.code == extraCurrencyCode }) {
            baseList + Currency(
                code = extraCurrencyCode!!,
                name = extraCurrencyName!!,
                flagEmoji = getFlagForCurrency(extraCurrencyCode!!)
            )
        } else {
            baseList
        }
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
        val pullProgress = (pullOffset.value / maxPullPx).coerceIn(0f, 1f)
        val pullHeightDp = with(density) { pullOffset.value.toDp() }
        val topSpacerHeight = (20.dp - (12.dp * pullProgress)).coerceAtLeast(8.dp)

        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
                    .nestedScroll(nestedScrollConnection)
                    .pointerInput(Unit) {
                        awaitEachGesture {
                            awaitFirstDown(requireUnconsumed = false)
                            touchStartedWithLogoDeployed = (isLatched || pullOffset.value > 0f)
                            coroutineScope.launch { pullOffset.stop() }
                            do {
                                val event = awaitPointerEvent()
                            } while (event.changes.any { it.pressed })

                            // Al soltar el dedo:
                            if (isLatched) {
                                // Se queda anclado en el techo
                                coroutineScope.launch {
                                    pullOffset.animateTo(maxPullPx, tween(durationMillis = 150, easing = FastOutSlowInEasing))
                                }
                            } else if (pullOffset.value > 0f) {
                                // Si no se tocó el límite (techo) o se desenganchó, se vuelve a esconder
                                hasVibratedThisPull = false
                                coroutineScope.launch {
                                    pullOffset.animateTo(0f, tween(durationMillis = 200, easing = FastOutSlowInEasing))
                                }
                            }
                        }
                    }
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Revelación lúdica de FINANCEZ al deslizar hacia abajo desde el tope
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(pullHeightDp)
                        .graphicsLayer {
                            alpha = pullProgress
                            scaleX = 0.65f + 0.35f * pullProgress
                            scaleY = 0.65f + 0.35f * pullProgress
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (pullOffset.value > 4f) {
                        FinanceZLogo(
                            baseFontSize = 18.sp,
                            accentFontSize = 24.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(topSpacerHeight))

                // 1. Cabecera editorial limpia centrada
                BrandHeader()

                Spacer(modifier = Modifier.height(32.dp))

                // 2. Tarjeta Hero con la divisa actualmente seleccionada
                SelectedCurrencyCard(currency = selectedCurrency)

                Spacer(modifier = Modifier.height(28.dp))

                // 3. Selección rápida de monedas frecuentes
                QuickSelectionSection(
                    currencies = quickSelectionCurrencies,
                    selectedCode = selectedCurrency.code,
                    onSelectCurrency = { currency ->
                        selectedCode = currency.code
                        selectedName = currency.name
                        if (CurrencyLoader.defaultCurrencies.none { it.code == currency.code }) {
                            extraCurrencyCode = currency.code
                            extraCurrencyName = currency.name
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 4. Botón de exploración del catálogo completo
                SearchTriggerField(
                    onClick = { showFullCatalogSheet = true }
                )

                Spacer(modifier = Modifier.height(innerPadding.calculateBottomPadding() + 16.dp))
            }
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
                if (CurrencyLoader.defaultCurrencies.none { it.code == currency.code }) {
                    extraCurrencyCode = currency.code
                    extraCurrencyName = currency.name
                }
                showFullCatalogSheet = false
            },
            sheetState = sheetState
        )
    }
}
