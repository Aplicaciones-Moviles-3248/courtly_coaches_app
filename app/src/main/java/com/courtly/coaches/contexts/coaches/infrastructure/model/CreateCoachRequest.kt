package com.courtly.coaches.contexts.coaches.infrastructure.model

import com.courtly.coaches.contexts.coaches.domain.model.CreateCoachParams

data class CreateCoachRequest(
    val name: String,
    val expertise: String,
    val phone: String,
    val userId: Long
) {
    companion object {
        fun fromDomain(params: CreateCoachParams): CreateCoachRequest {
            return CreateCoachRequest(
                name = params.name,
                expertise = params.expertise,
                phone = params.phone,
                userId = params.userId
            )
        }
    }
}