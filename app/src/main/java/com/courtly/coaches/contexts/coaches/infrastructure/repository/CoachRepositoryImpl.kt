package com.courtly.coaches.contexts.coaches.infrastructure.repository

import com.courtly.coaches.contexts.coaches.domain.model.Coach
import com.courtly.coaches.contexts.coaches.domain.model.CreateCoachParams
import com.courtly.coaches.contexts.coaches.domain.model.UpdateCoachParams
import com.courtly.coaches.contexts.coaches.domain.repository.CoachRepository
import com.courtly.coaches.contexts.coaches.infrastructure.model.CreateCoachRequest
import com.courtly.coaches.contexts.coaches.infrastructure.model.UpdateCoachRequest
import com.courtly.coaches.contexts.coaches.infrastructure.remote.CoachApiService

class CoachRepositoryImpl(
    private val apiService: CoachApiService
) : CoachRepository {

    override suspend fun getAllCoaches(): List<Coach> {
        return apiService
            .getAllCoaches()
            .map { it.toDomain() }
    }

    override suspend fun getCoachById(id: Long): Coach {
        return apiService
            .getCoachById(id)
            .toDomain()
    }

    override suspend fun getMyCoach(): Coach {
        return apiService
            .getMyCoach()
            .toDomain()
    }

    override suspend fun createCoach(
        params: CreateCoachParams
    ): Coach {
        val request = CreateCoachRequest.fromDomain(params)

        return apiService
            .createCoach(request)
            .toDomain()
    }

    override suspend fun updateCoach(
        id: Long,
        params: UpdateCoachParams
    ): Coach {
        val request = UpdateCoachRequest.fromDomain(params)

        return apiService
            .updateCoach(id, request)
            .toDomain()
    }

    override suspend fun deleteCoach(id: Long) {
        val response = apiService.deleteCoach(id)

        if (!response.isSuccessful) {
            throw IllegalStateException(
                "No se pudo eliminar el perfil del entrenador."
            )
        }
    }
}