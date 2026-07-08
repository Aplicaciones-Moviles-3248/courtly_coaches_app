package com.courtly.coaches.contexts.payments.infrastructure.model

import com.courtly.coaches.contexts.payments.domain.model.Payment

data class PaymentDto(
    val id: Long?,
    val amount: Double?,
    val paymentDate: String?,
    val status: String?,
    val contextType: String?,
    val bookingId: Long?,
    val trainingSessionId: Long?,
    val player: PersonSummaryDto?,
    val user: PersonSummaryDto?,
    val coach: PersonSummaryDto?,
    val court: CourtSummaryDto?
) {
    data class PersonSummaryDto(
        val id: Long?,
        val name: String?
    )

    data class CourtSummaryDto(
        val id: Long?,
        val name: String?
    )

    fun toDomain() =
        Payment(
            id = id ?: 0L,
            amount = amount ?: 0.0,
            paymentDate = paymentDate.orEmpty(),
            status = status.orEmpty(),
            contextType = contextType.orEmpty(),
            bookingId = bookingId,
            trainingSessionId = trainingSessionId,
            playerName = player?.name?.takeIf { it.isNotBlank() }
                ?: user?.name?.takeIf { it.isNotBlank() }
                ?: "Jugador",
            courtName = court?.name?.takeIf { it.isNotBlank() }
                ?: "Sesión de entrenamiento"
        )
}
