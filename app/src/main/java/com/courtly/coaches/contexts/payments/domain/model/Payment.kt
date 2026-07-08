package com.courtly.coaches.contexts.payments.domain.model

data class Payment(
    val id: Long,
    val amount: Double,
    val paymentDate: String,
    val status: String,
    val contextType: String,
    val bookingId: Long?,
    val trainingSessionId: Long?,
    val playerName: String,
    val courtName: String
)
