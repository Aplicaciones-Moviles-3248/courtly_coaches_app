package com.courtly.coaches.contexts.iam.infrastructure.model

data class SignUpRequest(
    val username: String,
    val password: String,
    val roles: List<String>
)