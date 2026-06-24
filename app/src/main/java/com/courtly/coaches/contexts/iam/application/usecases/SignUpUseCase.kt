package com.courtly.coaches.contexts.iam.application.usecases

import com.courtly.coaches.contexts.iam.domain.repository.AuthenticationRepository

class SignUpUseCase(
    private val repository: AuthenticationRepository
) {

    suspend operator fun invoke(
        username: String,
        password: String,
        roles: List<String>
    ): Long {
        return repository.signUp(
            username = username,
            password = password,
            roles = roles
        )
    }
}