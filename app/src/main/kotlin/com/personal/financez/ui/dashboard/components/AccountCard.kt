package com.personal.financez.ui.dashboard.components

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.personal.financez.core.utils.formatMoney
import com.personal.financez.data.local.AccountEntity
import com.personal.financez.data.local.AccountType

/**
 * Tarjeta visual individual para representar una cuenta registrada dentro de la cuadrícula.
 * Diseñada bajo la estética neobanco minimalista (Dark Hunter, Frosted Ice Mint, Vibrant Mint).
 */
@Composable
fun AccountCard(
    account: AccountEntity,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(115.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(colors.surface)
            .border(
                width = 1.dp,
                color = colors.outline.copy(alpha = 0.45f),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Fila superior: Tipo y distintivos (4x1000 / % E.A.)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val typeLabel = if (account.type == AccountType.EFECTIVO) "EFECTIVO" else "BANCO"
                Text(
                    text = typeLabel,
                    style = typography.labelSmall.copy(
                        letterSpacing = 0.5.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = colors.primary
                )

                if (account.annualEffectiveRate != null && account.annualEffectiveRate > 0.0) {
                    Text(
                        text = "${account.annualEffectiveRate}% EA",
                        style = typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = colors.onSurfaceVariant
                    )
                } else if (account.isGmfExempt) {
                    Text(
                        text = "4x1000",
                        style = typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        color = colors.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Nombre de la cuenta
            Text(
                text = account.name,
                style = typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Saldo monetario formateado
            Text(
                text = account.balance.formatMoney(account.currency),
                style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = colors.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
