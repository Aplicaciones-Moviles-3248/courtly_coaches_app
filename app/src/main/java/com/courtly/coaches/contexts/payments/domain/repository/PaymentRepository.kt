package com.courtly.coaches.contexts.payments.domain.repository

import com.courtly.coaches.contexts.payments.domain.model.Payment

interface PaymentRepository {
    suspend fun getReceivedPayments(): List<Payment>
}
