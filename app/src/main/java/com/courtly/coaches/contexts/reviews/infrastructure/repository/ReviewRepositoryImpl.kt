package com.courtly.coaches.contexts.reviews.infrastructure.repository

import com.courtly.coaches.contexts.reviews.domain.model.Review
import com.courtly.coaches.contexts.reviews.domain.repository.ReviewRepository
import com.courtly.coaches.contexts.reviews.infrastructure.remote.ReviewApiService

class ReviewRepositoryImpl(
    private val apiService: ReviewApiService
) : ReviewRepository {
    override suspend fun getAllReviews(): List<Review> {
        return apiService.getAllReviews().map { it.toDomain() }
    }
}
