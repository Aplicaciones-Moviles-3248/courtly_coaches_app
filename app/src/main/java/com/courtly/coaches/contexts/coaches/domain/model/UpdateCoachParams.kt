package com.courtly.coaches.contexts.coaches.domain.model

data class UpdateCoachParams(
    val name: String,
    val expertise: String,
    val phone: String
)