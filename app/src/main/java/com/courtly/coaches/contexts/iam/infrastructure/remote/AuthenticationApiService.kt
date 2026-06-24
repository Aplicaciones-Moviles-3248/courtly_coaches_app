package com.courtly.coaches.contexts.iam.infrastructure.remote

import com.courtly.coaches.contexts.iam.infrastructure.model.AuthenticatedUserDto
import com.courtly.coaches.contexts.iam.infrastructure.model.RegisteredUserDto
import com.courtly.coaches.contexts.iam.infrastructure.model.SignInRequest
import com.courtly.coaches.contexts.iam.infrastructure.model.SignUpRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationApiService {

    @POST("authentication/sign-in")
    suspend fun signIn(
        @Body request: SignInRequest
    ): AuthenticatedUserDto

    @POST("authentication/sign-up")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): RegisteredUserDto
}