package com.courtly.coaches.contexts.coaches.domain.model

data class CreateCoachParams(
    val name: String,
    val expertise: String,
    val phone: String,
    val userId: Long
)