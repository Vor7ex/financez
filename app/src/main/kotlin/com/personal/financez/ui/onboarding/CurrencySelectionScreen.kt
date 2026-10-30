package com.personal.financez.ui.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.financez.data.currencies.Currency
import com.personal.financez.data.currencies.CurrencyLoader
import com.personal.financez.data.currencies.getFlagForCurrency
import com.personal.financez.ui.theme.CreatoDisplay
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CurrencySelectionScreen(
    initialCurrency: String = "COP",
    onConfirm: (String) -> Unit
) {
    val context = LocalContext.current
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

                // 1. Cabecera editorial limpia centrada (sin "PASO 01")
                BrandHeader()

                Spacer(modifier = Modifier.height(32.dp))

                // 2. Tarjeta Hero con la divisa actualmente seleccionada (sin metadatos técnicos)
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

/**
 * Logotipo oficial de FinanceZ con acento tipográfico en las últimas dos letras "EZ",
 * sin el punto verde a la izquierda.
 */
@Composable
fun FinanceZLogo(
    modifier: Modifier = Modifier,
    baseFontSize: TextUnit = 18.sp,
    accentFontSize: TextUnit = 24.sp
) {
    val colors = MaterialTheme.colorScheme

    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    fontFamily = CreatoDisplay,
                    fontWeight = FontWeight.Black,
                    fontSize = baseFontSize,
                    letterSpacing = 2.sp,
                    color = colors.onBackground
                )
            ) {
                append("FINANC")
            }
            withStyle(
                SpanStyle(
                    fontFamily = CreatoDisplay,
                    fontWeight = FontWeight.Black,
                    fontSize = accentFontSize,
                    letterSpacing = 2.sp,
                    color = colors.primary
                )
            ) {
                append("EZ")
            }
        }
    )
}

/**
 * Cabecera editorial con el mensaje de bienvenida centrado.
 */
@Composable
fun BrandHeader(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bienvenido",
            style = typography.headlineLarge,
            color = colors.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Elige tu moneda principal. Podrás agregar otras monedas secundarias más adelante.",
            style = typography.bodyLarge,
            color = colors.onSurfaceVariant,
            textAlign = TextAlign.Center
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
    val typography = MaterialTheme.typography

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
                                style = typography.displayMedium,
                                color = colors.onSurface
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = targetCurrency.flagEmoji,
                                fontSize = 32.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = targetCurrency.name,
                            style = typography.titleLarge,
                            color = colors.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Formato de balance",
                    style = typography.labelSmall,
                    color = colors.onSurfaceVariant
                )
                Text(
                    text = "${currency.code} 1,250,000.00",
                    style = typography.labelMedium,
                    maxLines = 1,
                    color = colors.onSurface
                )
            }
        }
    }
}

/**
 * Sección de selección rápida con divisas frecuentes y la 6ta seleccionada si aplica.
 */
@Composable
fun QuickSelectionSection(
    currencies: List<Currency>,
    selectedCode: String,
    onSelectCurrency: (Currency) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        currencies.forEach { currency ->
            val isSelected = currency.code == selectedCode

            TactileCurrencyItem(
                currency = currency,
                isSelected = isSelected,
                onSelect = { onSelectCurrency(currency) }
            )
        }
    }
}

/**
 * Botón para abrir el catálogo completo de divisas, con las mismas dimensiones,
 * estilo, color y tipografía que los selectores de moneda.
 */
@Composable
fun SearchTriggerField(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isInteracting = isPressed || isHovered

    val itemScale by animateFloatAsState(
        targetValue = if (isInteracting) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "searchBtnScale"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isInteracting) colors.outline.copy(alpha = 0.40f) else colors.outline.copy(alpha = 0.18f),
        animationSpec = tween(150),
        label = "searchBorder"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .graphicsLayer {
                scaleX = itemScale
                scaleY = itemScale
            }
            .clip(RoundedCornerShape(14.dp))
            .background(colors.surface)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .semantics {
                role = Role.Button
                onClick {
                    onClick()
                    true
                }
            }
            .pointerInput(onClick) {
                detectTapGestures(
                    onPress = { offset ->
                        val press = PressInteraction.Press(offset)
                        interactionSource.emit(press)
                        if (tryAwaitRelease()) {
                            interactionSource.emit(PressInteraction.Release(press))
                        } else {
                            interactionSource.emit(PressInteraction.Cancel(press))
                        }
                    },
                    onTap = { onClick() }
                )
            }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = colors.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = "Buscar en otra divisa",
            style = typography.titleMedium,
            color = colors.onSurface
        )
    }
}

/**
 * Item táctil individual para listas de divisas con micro-interacción de pulsación previa (hover/press).
 */
@Composable
fun TactileCurrencyItem(
    currency: Currency,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val isInteracting = isPressed || isHovered

    val itemScale by animateFloatAsState(
        targetValue = if (isInteracting) 0.98f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "itemScale"
    )

    val containerColor by animateColorAsState(
        targetValue = if (isSelected) colors.primary.copy(alpha = 0.08f) else colors.surface,
        animationSpec = tween(150),
        label = "itemBg"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            isSelected -> colors.primary
            isInteracting -> colors.outline.copy(alpha = 0.40f)
            else -> colors.outline.copy(alpha = 0.18f)
        },
        animationSpec = tween(150),
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
            .height(64.dp)
            .graphicsLayer {
                scaleX = itemScale
                scaleY = itemScale
            }
            .clip(RoundedCornerShape(14.dp))
            .background(containerColor)
            .border(borderWidth, borderColor, RoundedCornerShape(14.dp))
            .semantics {
                role = Role.Button
                onClick {
                    onSelect()
                    true
                }
            }
            .pointerInput(onSelect) {
                detectTapGestures(
                    onPress = { offset ->
                        val press = PressInteraction.Press(offset)
                        interactionSource.emit(press)
                        if (tryAwaitRelease()) {
                            interactionSource.emit(PressInteraction.Release(press))
                        } else {
                            interactionSource.emit(PressInteraction.Cancel(press))
                        }
                    },
                    onTap = {
                        onSelect()
                    }
                )
            }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .width(84.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(colors.background)
                .border(
                    width = 0.5.dp,
                    color = if (isSelected) colors.primary.copy(alpha = 0.4f) else colors.outline.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = currency.flagEmoji,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = currency.code,
                style = typography.labelMedium,
                maxLines = 1,
                color = if (isSelected) colors.primary else colors.onSurface
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = currency.name,
            style = typography.titleMedium,
            color = if (isSelected) colors.onSurface else colors.onSurfaceVariant,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Box(
            modifier = Modifier
                .size(24.dp)
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
                    modifier = Modifier.size(16.dp)
                )
            }
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

/**
 * Botón estático y reutilizable de confirmación con color sólido institucional.
 */
@Composable
fun ConfirmButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.primary,
            contentColor = colors.onPrimary
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = "CONFIRMAR",
            style = typography.labelLarge.copy(
                fontSize = 16.sp,
                letterSpacing = 1.5.sp
            )
        )
    }
}

/**
 * Barra de navegación inferior con línea divisoria sólida que aloja el ConfirmButton.
 */
@Composable
fun BottomConfirmationBar(
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.background)
    ) {
        HorizontalDivider(
            color = colors.outline.copy(alpha = 0.35f),
            thickness = 1.dp
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 14.dp)
        ) {
            ConfirmButton(onClick = onConfirm)
        }
    }
}
