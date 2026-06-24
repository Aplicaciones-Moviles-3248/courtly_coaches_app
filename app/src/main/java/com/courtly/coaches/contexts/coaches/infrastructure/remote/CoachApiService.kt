package com.courtly.coaches.contexts.coaches.infrastructure.remote

import com.courtly.coaches.contexts.coaches.infrastructure.model.CoachDto
import com.courtly.coaches.contexts.coaches.infrastructure.model.CreateCoachRequest
import com.courtly.coaches.contexts.coaches.infrastructure.model.UpdateCoachRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CoachApiService {

    @GET("coaches")
    suspend fun getAllCoaches(): List<CoachDto>

    @GET("coaches/{id}")
    suspend fun getCoachById(
        @Path("id") id: Long
    ): CoachDto

    @GET("coaches/me")
    suspend fun getMyCoach(): CoachDto

    @POST("coaches")
    suspend fun createCoach(
        @Body request: CreateCoachRequest
    ): CoachDto

    @PUT("coaches/{id}")
    suspend fun updateCoach(
        @Path("id") id: Long,
        @Body request: UpdateCoachRequest
    ): CoachDto

    @DELETE("coaches/{id}")
    suspend fun deleteCoach(
        @Path("id") id: Long
    ): Response<Unit>
}