package com.courtly.coaches.contexts.iam.infrastructure.repository

import com.courtly.coaches.contexts.iam.domain.model.AuthenticatedUser
import com.courtly.coaches.contexts.iam.domain.repository.AuthenticationRepository
import com.courtly.coaches.contexts.iam.infrastructure.model.SignInRequest
import com.courtly.coaches.contexts.iam.infrastructure.model.SignUpRequest
import com.courtly.coaches.contexts.iam.infrastructure.remote.AuthenticationApiService
import com.courtly.coaches.shared.infrastructure.storage.SessionStorage

class AuthenticationRepositoryImpl(
    private val apiService: AuthenticationApiService,
    private val sessionStorage: SessionStorage
) : AuthenticationRepository {

    override suspend fun signIn(
        username: String,
        password: String
    ): AuthenticatedUser {
        val response = apiService.signIn(
            SignInRequest(
                username = username,
                password = password
            )
        )

        sessionStorage.saveSession(
            userId = response.id,
            username = response.username,
            token = response.token
        )

        return AuthenticatedUser(
            id = response.id,
            username = response.username,
            token = response.token
        )
    }

    override suspend fun signUp(
        username: String,
        password: String,
        roles: List<String>
    ): Long {
        val response = apiService.signUp(
            SignUpRequest(
                username = username,
                password = password,
                roles = roles
            )
        )

        return response.id
    }

    override fun hasActiveSession(): Boolean {
        return sessionStorage.hasActiveSession()
    }

    override fun signOut() {
        sessionStorage.clearSession()
    }
}