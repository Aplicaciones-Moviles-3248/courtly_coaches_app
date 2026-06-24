package com.courtly.coaches.contexts.iam.infrastructure.model

data class SignInRequest(
    val username: String,
    val password: String
)