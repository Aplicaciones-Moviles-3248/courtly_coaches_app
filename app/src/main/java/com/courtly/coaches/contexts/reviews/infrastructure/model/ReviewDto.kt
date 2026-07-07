package com.courtly.coaches.contexts.reviews.infrastructure.model

import com.courtly.coaches.contexts.reviews.domain.model.Review

data class ReviewDto(
    val id: Long,
    val score: Int,
    val comment: String,
    val targetId: Long,
    val targetType: String,
    val user: UserSummaryDto?
) {
    data class UserSummaryDto(val id: Long, val name: String)

    fun toDomain(): Review {
        return Review(
            id = id,
            score = score,
            comment = comment,
            targetId = targetId,
            targetType = targetType,
            userName = user?.name ?: "Deportista"
        )
    }
}
