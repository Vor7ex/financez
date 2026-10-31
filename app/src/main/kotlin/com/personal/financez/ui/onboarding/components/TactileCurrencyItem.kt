package com.personal.financez.ui.onboarding.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.financez.core.currency.Currency
import com.personal.financez.ui.components.tactilePress

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

    val containerColor = if (isSelected) colors.primary.copy(alpha = 0.08f) else colors.surface
    val borderColor = if (isSelected) colors.primary else colors.outline.copy(alpha = 0.18f)
    val borderWidth = if (isSelected) 1.5.dp else 1.dp

    val checkScale by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.85f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "checkScale"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .tactilePress(targetScale = 0.98f, onClick = onSelect)
            .clip(RoundedCornerShape(14.dp))
            .background(containerColor)
            .border(borderWidth, borderColor, RoundedCornerShape(14.dp))
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
