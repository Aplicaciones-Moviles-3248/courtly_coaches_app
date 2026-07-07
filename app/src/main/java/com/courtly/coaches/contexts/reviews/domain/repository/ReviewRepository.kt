package com.courtly.coaches.contexts.reviews.domain.repository

import com.courtly.coaches.contexts.reviews.domain.model.Review

interface ReviewRepository {
    suspend fun getAllReviews(): List<Review>
}
