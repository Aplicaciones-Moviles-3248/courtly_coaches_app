package com.courtly.coaches.contexts.iam.application.usecases

import com.courtly.coaches.contexts.iam.domain.model.AuthenticatedUser
import com.courtly.coaches.contexts.iam.domain.repository.AuthenticationRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

private class FakeAuthenticationRepository : AuthenticationRepository {
    var signInResult: AuthenticatedUser = AuthenticatedUser(id = 1, username = "coach", token = "token-123")
    var signUpResult: Long = 42L
    var activeSession: Boolean = false
    var signOutCalled = false
    var error: Throwable? = null

    var lastSignInUsername: String? = null
    var lastSignInPassword: String? = null
    var lastSignUpRoles: List<String>? = null

    override suspend fun signIn(username: String, password: String): AuthenticatedUser {
        error?.let { throw it }
        lastSignInUsername = username
        lastSignInPassword = password
        return signInResult
    }

    override suspend fun signUp(username: String, password: String, roles: List<String>): Long {
        error?.let { throw it }
        lastSignUpRoles = roles
        return signUpResult
    }

    override fun hasActiveSession(): Boolean = activeSession

    override fun signOut() {
        signOutCalled = true
    }
}

class AuthenticationUseCasesTest {

    @Test
    fun `SignInUseCase delega credenciales al repositorio y retorna el usuario autenticado`() = runBlocking {
        val repository = FakeAuthenticationRepository()
        val useCase = SignInUseCase(repository)

        val result = useCase("coach1", "secret")

        assertEquals("coach1", repository.lastSignInUsername)
        assertEquals("secret", repository.lastSignInPassword)
        assertEquals(repository.signInResult, result)
    }

    @Test(expected = IllegalStateException::class)
    fun `SignInUseCase propaga el error cuando el repositorio falla`(): Unit = runBlocking {
        val repository = FakeAuthenticationRepository().apply { error = IllegalStateException("401") }
        val useCase = SignInUseCase(repository)

        useCase("coach1", "wrong-password")
        Unit
    }

    @Test
    fun `SignUpUseCase envia username, password y roles, y retorna el id creado`() = runBlocking {
        val repository = FakeAuthenticationRepository()
        val useCase = SignUpUseCase(repository)

        val result = useCase("newcoach", "secret", listOf("ROLE_INSTRUCTOR"))

        assertEquals(listOf("ROLE_INSTRUCTOR"), repository.lastSignUpRoles)
        assertEquals(42L, result)
    }

    @Test
    fun `CheckSessionUseCase retorna true cuando hay sesion activa`() {
        val repository = FakeAuthenticationRepository().apply { activeSession = true }
        val useCase = CheckSessionUseCase(repository)

        assertTrue(useCase())
    }

    @Test
    fun `CheckSessionUseCase retorna false cuando no hay sesion`() {
        val repository = FakeAuthenticationRepository().apply { activeSession = false }
        val useCase = CheckSessionUseCase(repository)

        assertFalse(useCase())
    }

    @Test
    fun `SignOutUseCase pide al repositorio cerrar la sesion`() {
        val repository = FakeAuthenticationRepository()
        val useCase = SignOutUseCase(repository)

        useCase()

        assertTrue(repository.signOutCalled)
    }
}
