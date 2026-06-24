package com.courtly.coaches.contexts.coaches.infrastructure.model

import com.courtly.coaches.contexts.coaches.domain.model.Coach

data class CoachDto(
    val id: Long,
    val name: String,
    val expertise: String,
    val phone: String,
    val userId: Long
) {
    fun toDomain(): Coach {
        return Coach(
            id = id,
            name = name,
            expertise = expertise,
            phone = phone,
            userId = userId
        )
    }
}