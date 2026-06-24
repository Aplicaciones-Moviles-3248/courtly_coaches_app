package com.courtly.coaches.contexts.availabilities.infrastructure.repository

import com.courtly.coaches.contexts.availabilities.domain.model.Availability
import com.courtly.coaches.contexts.availabilities.domain.model.CreateAvailabilityParams
import com.courtly.coaches.contexts.availabilities.domain.model.UpdateAvailabilityParams
import com.courtly.coaches.contexts.availabilities.domain.repository.AvailabilityRepository
import com.courtly.coaches.contexts.availabilities.infrastructure.model.CreateAvailabilityRequest
import com.courtly.coaches.contexts.availabilities.infrastructure.model.UpdateAvailabilityRequest
import com.courtly.coaches.contexts.availabilities.infrastructure.remote.AvailabilityApiService

class AvailabilityRepositoryImpl(
    private val apiService: AvailabilityApiService
) : AvailabilityRepository {

    override suspend fun getAllAvailabilities(): List<Availability> {
        return apiService.getAllAvailabilities().map { it.toDomain() }
    }

    override suspend fun getMyAvailabilities(): List<Availability> {
        return apiService.getMyAvailabilities().map { it.toDomain() }
    }

    override suspend fun getAvailabilityById(id: Long): Availability {
        return apiService.getAvailabilityById(id).toDomain()
    }

    override suspend fun createAvailability(params: CreateAvailabilityParams): Availability {
        val request = CreateAvailabilityRequest.fromDomain(params)
        return apiService.createAvailability(request).toDomain()
    }

    override suspend fun updateAvailability(
        id: Long,
        params: UpdateAvailabilityParams
    ): Availability {
        val request = UpdateAvailabilityRequest.fromDomain(params)
        return apiService.updateAvailability(id, request).toDomain()
    }

    override suspend fun deleteAvailability(id: Long) {
        val response = apiService.deleteAvailability(id)

        if (!response.isSuccessful) {
            throw IllegalStateException(
                "No se pudo eliminar la disponibilidad."
            )
        }
    }
}
