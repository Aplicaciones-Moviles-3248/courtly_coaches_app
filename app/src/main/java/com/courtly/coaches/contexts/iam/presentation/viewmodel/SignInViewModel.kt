package com.courtly.coaches.contexts.iam.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courtly.coaches.contexts.coaches.application.usecases.CreateCoachUseCase
import com.courtly.coaches.contexts.coaches.domain.model.CreateCoachParams
import com.courtly.coaches.contexts.iam.application.usecases.SignInUseCase
import com.courtly.coaches.contexts.iam.application.usecases.SignUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import android.util.Log
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

data class SignInUiState(
    val username: String = "",
    val password: String = "",

    val registerName: String = "",
    val registerUsername: String = "",
    val registerPhone: String = "",
    val registerExpertise: String = "",
    val registerPassword: String = "",

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class SignInViewModel(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val createCoachUseCase: CreateCoachUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignInUiState())

    val uiState: StateFlow<SignInUiState> =
        _uiState.asStateFlow()

    fun onUsernameChanged(value: String) {
        _uiState.update {
            it.copy(
                username = value,
                errorMessage = null
            )
        }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update {
            it.copy(
                password = value,
                errorMessage = null
            )
        }
    }

    fun onRegisterNameChanged(value: String) {
        _uiState.update {
            it.copy(
                registerName = value,
                errorMessage = null
            )
        }
    }

    fun onRegisterUsernameChanged(value: String) {
        _uiState.update {
            it.copy(
                registerUsername = value,
                errorMessage = null
            )
        }
    }

    fun onRegisterPhoneChanged(value: String) {
        _uiState.update {
            it.copy(
                registerPhone = value,
                errorMessage = null
            )
        }
    }

    fun onRegisterExpertiseChanged(value: String) {
        _uiState.update {
            it.copy(
                registerExpertise = value,
                errorMessage = null
            )
        }
    }

    fun onRegisterPasswordChanged(value: String) {
        _uiState.update {
            it.copy(
                registerPassword = value,
                errorMessage = null
            )
        }
    }

    fun signIn() {
        val state = _uiState.value

        val username = state.username.trim()
        val password = state.password.trim()

        if (username.isBlank()) {
            showError("Ingresa tu usuario o correo.")
            return
        }

        if (password.isBlank()) {
            showError("Ingresa tu contraseña.")
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                signInUseCase(
                    username = username,
                    password = password
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
            } catch (error: Exception) {
                logException(error)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = resolveErrorMessage(
                            error = error,
                            defaultMessage =
                                "No se pudo iniciar sesión. Verifica tus credenciales."
                        )
                    )
                }
            }
        }
    }

    fun signUpCoach() {
        val state = _uiState.value

        val name = state.registerName.trim()
        val username = state.registerUsername.trim()
        val phone = state.registerPhone.trim()
        val expertise = state.registerExpertise.trim()
        val password = state.registerPassword.trim()

        when {
            name.length < 3 -> {
                showError("Ingresa un nombre válido.")
                return
            }

            username.isBlank() -> {
                showError("Ingresa un usuario o correo.")
                return
            }

            phone.length < 7 -> {
                showError("Ingresa un teléfono válido.")
                return
            }

            expertise.isBlank() -> {
                showError("Ingresa tu especialidad.")
                return
            }

            password.length < 6 -> {
                showError(
                    "La contraseña debe tener al menos 6 caracteres."
                )
                return
            }
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                val userId = signUpUseCase(
                    username = username,
                    password = password,
                    roles = listOf("ROLE_INSTRUCTOR")
                )

                signInUseCase(
                    username = username,
                    password = password
                )

                createCoachUseCase(
                    CreateCoachParams(
                        name = name,
                        expertise = expertise,
                        phone = phone,
                        userId = userId
                    )
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
            } catch (error: Exception) {
                logException(error)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = resolveErrorMessage(
                            error = error,
                            defaultMessage =
                                "No se pudo crear la cuenta de entrenador."
                        )
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }

    fun consumeSuccess() {
        _uiState.update {
            it.copy(isSuccess = false)
        }
    }

    private fun showError(message: String) {
        _uiState.update {
            it.copy(errorMessage = message)
        }
    }

    private fun resolveErrorMessage(
        error: Exception,
        defaultMessage: String
    ): String {
        return when (error) {
            is UnknownHostException -> {
                "No se pudo encontrar el servidor. Verifica tu conexión a Internet."
            }

            is SocketTimeoutException -> {
                "El servidor tardó demasiado en responder. Inténtalo nuevamente."
            }

            is ConnectException -> {
                "No se pudo establecer conexión con el servidor."
            }

            is SSLHandshakeException -> {
                "No se pudo establecer una conexión segura con el servidor."
            }

            is HttpException -> {
                val code = error.code()
                // Try to extract error body safely
                val errorBody = try {
                    error.response()?.errorBody()?.string()
                } catch (e: Exception) {
                    null
                }

                when (code) {
                    400 -> "Los datos ingresados no son válidos."
                    401 -> "Las credenciales o la sesión no son válidas."
                    403 -> "No tienes permisos para realizar esta acción."
                    404 -> "No se encontró el recurso solicitado."
                    409 -> "El usuario ya se encuentra registrado."
                    500 -> "Ocurrió un error interno en el servidor."
                    502, 503, 504 -> "El servidor no está disponible temporalmente. Inténtalo nuevamente en unos minutos."
                    else -> {
                        // If backend provided a message in errorBody, prefer a short message
                        if (!errorBody.isNullOrBlank()) {
                            "${defaultMessage} Código: $code. Detalle: ${shorten(errorBody)}"
                        } else {
                            "$defaultMessage Código: $code."
                        }
                    }
                }
            }

            is IOException -> {
                // Generic IO exception fallback
                "No se pudo conectar con el servidor."
            }

            else -> defaultMessage
        }
    }

    private fun shorten(s: String, max: Int = 200): String {
        return if (s.length <= max) s else s.substring(0, max) + "..."
    }

    private fun logException(error: Exception) {
        try {
            when (error) {
                is HttpException -> {
                    val response = error.response()
                    val url = response?.raw()?.request?.url
                    val method = response?.raw()?.request?.method
                    val code = error.code()
                    val errorBody = try { response?.errorBody()?.string() } catch (e: Exception) { "<no body>" }

                    Log.e("SignInViewModel", "HttpException: code=$code method=$method url=$url message=${error.message()}")
                    Log.e("SignInViewModel", "errorBody=" + shorten(errorBody ?: ""))
                }

                else -> {
                    Log.e("SignInViewModel", "Exception: class=${error::class.java.simpleName} message=${error.message}")
                }
            }
        } catch (e: Exception) {
            Log.e("SignInViewModel", "Failed to log exception: ${e.message}")
        }
    }
}