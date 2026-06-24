package com.courtly.coaches.contexts.iam.domain.repository

import com.courtly.coaches.contexts.iam.domain.model.AuthenticatedUser

interface AuthenticationRepository {

    suspend fun signIn(
        username: String,
        password: String
    ): AuthenticatedUser

    suspend fun signUp(
        username: String,
        password: String,
        roles: List<String>
    ): Long

    fun hasActiveSession(): Boolean

    fun signOut()
}