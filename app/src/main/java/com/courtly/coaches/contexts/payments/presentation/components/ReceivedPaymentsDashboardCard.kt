package com.courtly.coaches.contexts.payments.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.courtly.coaches.contexts.payments.domain.model.Payment
import com.courtly.coaches.contexts.payments.presentation.viewmodel.ReceivedPaymentsUiState
import com.courtly.coaches.ui.theme.Border
import com.courtly.coaches.ui.theme.Primary
import com.courtly.coaches.ui.theme.Spacing
import com.courtly.coaches.ui.theme.TextPrimary
import com.courtly.coaches.ui.theme.TextSecondary

@Composable
fun ReceivedPaymentsDashboardCard(
    uiState: ReceivedPaymentsUiState,
    onOpenHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White),
        shape = RoundedCornerShape(18.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Border, RoundedCornerShape(18.dp))
            .clickable(onClick = onOpenHistory)
    ) {
        Column(
            modifier = Modifier.padding(Spacing.md)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Pagos recibidos",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Ver historial",
                    color = Primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(Spacing.sm))

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Primary,
                            strokeWidth = 2.dp
                        )
                    }
                }

                uiState.payments.isNotEmpty() -> {
                    CompactPaymentPreview(payment = uiState.payments.first())
                }

                else -> {
                    Text(
                        text = "Aún no tienes pagos recibidos.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun CompactPaymentPreview(
    payment: Payment
) {
    val dateLabel = formatPaymentDate(payment.paymentDate)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = androidx.compose.ui.graphics.Color(0xFFF8FAFC),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(Spacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = formatSoles(payment.amount),
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = payment.playerName,
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = payment.courtName,
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }

        Spacer(modifier = Modifier.width(Spacing.sm))

        Box(
            modifier = Modifier
                .background(
                    color = Primary.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(999.dp)
                )
                .padding(
                    horizontal = 10.dp,
                    vertical = 6.dp
                )
        ) {
            Text(
                text = dateLabel,
                color = Primary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
