package com.courtly.coaches.contexts.coaches.infrastructure.model

import com.courtly.coaches.contexts.coaches.domain.model.UpdateCoachParams

data class UpdateCoachRequest(
    val name: String,
    val expertise: String,
    val phone: String
) {
    companion object {
        fun fromDomain(params: UpdateCoachParams): UpdateCoachRequest {
            return UpdateCoachRequest(
                name = params.name,
                expertise = params.expertise,
                phone = params.phone
            )
        }
    }
}