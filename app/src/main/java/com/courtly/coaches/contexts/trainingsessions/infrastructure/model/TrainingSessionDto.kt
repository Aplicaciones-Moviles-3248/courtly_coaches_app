package com.courtly.coaches.contexts.trainingsessions.infrastructure.model

import com.courtly.coaches.contexts.trainingsessions.domain.model.TrainingSession
import com.courtly.coaches.contexts.trainingsessions.domain.model.TrainingSessionStatus
import java.math.BigDecimal

data class TrainingSessionDto(
    val id: Long,
    val startTime: String,
    val endTime: String,
    val status: String,
    val price: BigDecimal,
    val player: PlayerSummaryDto?,
    val court: CourtSummaryDto?,
    val availabilityId: Long?
) {
    data class PlayerSummaryDto(val id: Long, val name: String)
    data class CourtSummaryDto(val id: Long, val name: String)

    fun toDomain(): TrainingSession {
        val mappedStatus = try {
            TrainingSessionStatus.valueOf(status)
        } catch (e: Exception) {
            TrainingSessionStatus.PENDING
        }
        return TrainingSession(
            id = id,
            startTime = startTime,
            endTime = endTime,
            status = mappedStatus,
            price = price,
            playerName = player?.name ?: "Deportista",
            courtName = court?.name ?: "Cancha",
            availabilityId = availabilityId
        )
    }
}
