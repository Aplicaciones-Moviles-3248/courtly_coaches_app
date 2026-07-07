package com.courtly.coaches.contexts.reviews.domain.model

/**
 * Entidad de dominio que representa una Reseña recibida por el entrenador (USM16).
 */
data class Review(
    val id: Long,
    val score: Int,
    val comment: String,
    val targetId: Long,
    val targetType: String,
    val userName: String
)
