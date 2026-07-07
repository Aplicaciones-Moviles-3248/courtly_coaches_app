package com.courtly.coaches.contexts.reviews.infrastructure.remote

import com.courtly.coaches.contexts.reviews.infrastructure.model.ReviewDto
import retrofit2.http.GET

interface ReviewApiService {
    @GET("reviews")
    suspend fun getAllReviews(): List<ReviewDto>
}
