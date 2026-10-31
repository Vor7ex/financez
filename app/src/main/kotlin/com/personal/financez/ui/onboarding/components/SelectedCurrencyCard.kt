package com.personal.financez.ui.onboarding.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.financez.core.currency.Currency

/**
 * Tarjeta Hero que destaca la moneda seleccionada de manera limpia y sin ruidos técnicos.
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
