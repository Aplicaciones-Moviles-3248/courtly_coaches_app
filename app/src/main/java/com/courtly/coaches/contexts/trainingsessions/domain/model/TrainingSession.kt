package com.courtly.coaches.contexts.trainingsessions.domain.model

import java.math.BigDecimal

data class TrainingSession(
    val id: Long,
    val startTime: String,
    val endTime: String,
    val status: TrainingSessionStatus,
    val price: BigDecimal,
    val playerName: String,
    val courtName: String = "",
    val availabilityId: Long?
)
