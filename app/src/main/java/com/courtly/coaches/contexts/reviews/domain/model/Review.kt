package com.courtly.coaches.contexts.reviews.domain.model

data class Review(
    val id: Long,
    val score: Int,
    val comment: String,
    val targetId: Long,
    val targetType: String,
    val userName: String
)
