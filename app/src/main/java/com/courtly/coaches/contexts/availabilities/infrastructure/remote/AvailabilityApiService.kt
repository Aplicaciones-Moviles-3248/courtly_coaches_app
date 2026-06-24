package com.courtly.coaches.contexts.availabilities.infrastructure.remote

import com.courtly.coaches.contexts.availabilities.infrastructure.model.AvailabilityDto
import com.courtly.coaches.contexts.availabilities.infrastructure.model.CreateAvailabilityRequest
import com.courtly.coaches.contexts.availabilities.infrastructure.model.UpdateAvailabilityRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AvailabilityApiService {

    @GET("availabilities")
    suspend fun getAllAvailabilities(): List<AvailabilityDto>

    @GET("availabilities/me")
    suspend fun getMyAvailabilities(): List<AvailabilityDto>

    @GET("availabilities/{id}")
    suspend fun getAvailabilityById(
        @Path("id") id: Long
    ): AvailabilityDto

    @POST("availabilities")
    suspend fun createAvailability(
        @Body request: CreateAvailabilityRequest
    ): AvailabilityDto

    @PUT("availabilities/{id}")
    suspend fun updateAvailability(
        @Path("id") id: Long,
        @Body request: UpdateAvailabilityRequest
    ): AvailabilityDto

    @DELETE("availabilities/{id}")
    suspend fun deleteAvailability(
        @Path("id") id: Long
    ): Response<Unit>
}
