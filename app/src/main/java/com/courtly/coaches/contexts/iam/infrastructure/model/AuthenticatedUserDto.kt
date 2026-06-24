package com.courtly.coaches.contexts.iam.infrastructure.model

data class AuthenticatedUserDto(
    val id: Int,
    val username: String,
    val token: String
)