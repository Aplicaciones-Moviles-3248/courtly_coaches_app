package com.courtly.coaches.contexts.coaches.domain.repository

import com.courtly.coaches.contexts.coaches.domain.model.Coach
import com.courtly.coaches.contexts.coaches.domain.model.CreateCoachParams
import com.courtly.coaches.contexts.coaches.domain.model.UpdateCoachParams

interface CoachRepository {

    suspend fun getAllCoaches(): List<Coach>

    suspend fun getCoachById(id: Long): Coach

    suspend fun getMyCoach(): Coach

    suspend fun createCoach(
        params: CreateCoachParams
    ): Coach

    suspend fun updateCoach(
        id: Long,
        params: UpdateCoachParams
    ): Coach

    suspend fun deleteCoach(id: Long)
}