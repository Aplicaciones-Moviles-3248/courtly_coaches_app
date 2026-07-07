package com.courtly.coaches.contexts.trainingsessions.infrastructure.remote

import com.courtly.coaches.contexts.trainingsessions.infrastructure.model.TrainingSessionDto
import com.courtly.coaches.contexts.trainingsessions.infrastructure.model.RejectSessionRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TrainingSessionApiService {

    @GET("training-sessions")
    suspend fun getMyTrainingSessions(): List<TrainingSessionDto>

    @POST("training-sessions/{id}/accept")
    suspend fun acceptTrainingSession(
        @Path("id") id: Long
    ): TrainingSessionDto

    @POST("training-sessions/{id}/reject")
    suspend fun rejectTrainingSession(
        @Path("id") id: Long,
        @Body request: RejectSessionRequest? = null
    ): TrainingSessionDto
}
