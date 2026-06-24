package com.courtly.coaches.contexts.coaches.domain.model

data class Coach(
    val id: Long,
    val name: String,
    val expertise: String,
    val phone: String,
    val userId: Long
)