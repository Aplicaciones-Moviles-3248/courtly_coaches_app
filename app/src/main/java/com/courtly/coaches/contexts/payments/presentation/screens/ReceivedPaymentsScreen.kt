package com.courtly.coaches.contexts.payments.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.courtly.coaches.contexts.payments.presentation.components.PaymentReceivedCard
import com.courtly.coaches.contexts.payments.presentation.viewmodel.ReceivedPaymentsViewModel
import com.courtly.coaches.ui.theme.Background
import com.courtly.coaches.ui.theme.Border
import com.courtly.coaches.ui.theme.Card
import com.courtly.coaches.ui.theme.Primary
import com.courtly.coaches.ui.theme.Spacing
import com.courtly.coaches.ui.theme.TextPrimary
import com.courtly.coaches.ui.theme.TextSecondary

@Composable
fun ReceivedPaymentsScreen(
    viewModel: ReceivedPaymentsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadReceivedPayments()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(Spacing.md)
    ) {
        Text(
            text = "PAGOS POR SESIONES",
            color = TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.4.sp
        )
        Spacer(modifier = Modifier.height(Spacing.xs))
        Text(
            text = "Pagos recibidos",
            color = TextPrimary,
            fontSize = 28.sp,
            fontWeight = FontWeight.Black
        )
        Spacer(modifier = Modifier.height(Spacing.md))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Card, RoundedCornerShape(22.dp))
                .border(1.dp, Border, RoundedCornerShape(22.dp))
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Primary)
                    }
                }

                uiState.errorMessage != null -> {
                    PaymentsMessage(
                        title = "Aún no tienes pagos recibidos."
                    )
                }

                uiState.payments.isEmpty() -> {
                    PaymentsMessage(
                        title = "Aún no tienes pagos recibidos."
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(Spacing.md),
                        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
                    ) {
                        items(uiState.payments) { payment ->
                            PaymentReceivedCard(payment = payment)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentsMessage(
    title: String,
    body: String? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.lg),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            if (body != null) {
                Spacer(modifier = Modifier.height(Spacing.xs))
                Text(
                    text = body,
                    color = TextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
