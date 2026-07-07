package com.courtly.coaches.contexts.reviews.application.usecases

import com.courtly.coaches.contexts.reviews.domain.model.Review
import com.courtly.coaches.contexts.reviews.domain.repository.ReviewRepository

class GetCoachReviewsUseCase(
    private val repository: ReviewRepository
) {
    suspend fun execute(coachId: Long): List<Review> {
        return repository.getAllReviews().filter {
            it.targetType == "COACH" && it.targetId == coachId
        }
    }
}
