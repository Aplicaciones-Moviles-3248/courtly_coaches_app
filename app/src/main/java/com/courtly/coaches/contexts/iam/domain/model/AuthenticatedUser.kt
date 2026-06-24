package com.courtly.coaches.contexts.iam.domain.model

data class AuthenticatedUser(
    val id: Int,
    val username: String,
    val token: String
)