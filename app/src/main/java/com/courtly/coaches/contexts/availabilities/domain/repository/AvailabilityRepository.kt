package com.courtly.coaches.contexts.availabilities.domain.repository

import com.courtly.coaches.contexts.availabilities.domain.model.Availability
import com.courtly.coaches.contexts.availabilities.domain.model.CreateAvailabilityParams
import com.courtly.coaches.contexts.availabilities.domain.model.UpdateAvailabilityParams

interface AvailabilityRepository {

    suspend fun getAllAvailabilities(): List<Availability>

    suspend fun getMyAvailabilities(): List<Availability>

    suspend fun getAvailabilityById(id: Long): Availability

    suspend fun createAvailability(params: CreateAvailabilityParams): Availability

    suspend fun updateAvailability(
        id: Long,
        params: UpdateAvailabilityParams
    ): Availability

    suspend fun deleteAvailability(id: Long)
}
