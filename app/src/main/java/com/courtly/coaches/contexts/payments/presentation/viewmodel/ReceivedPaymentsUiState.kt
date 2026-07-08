package com.courtly.coaches.contexts.payments.presentation.viewmodel

import com.courtly.coaches.contexts.payments.domain.model.Payment

data class ReceivedPaymentsUiState(
    val isLoading: Boolean = false,
    val payments: List<Payment> = emptyList(),
    val errorMessage: String? = null
)
