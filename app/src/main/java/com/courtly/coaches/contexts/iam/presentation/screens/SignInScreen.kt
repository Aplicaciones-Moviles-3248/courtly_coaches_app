package com.courtly.coaches.contexts.iam.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.courtly.coaches.R
import com.courtly.coaches.contexts.iam.presentation.viewmodel.SignInUiState
import com.courtly.coaches.contexts.iam.presentation.viewmodel.SignInViewModel
import com.courtly.coaches.ui.theme.Background
import com.courtly.coaches.ui.theme.Border
import com.courtly.coaches.ui.theme.DarkNavy
import com.courtly.coaches.ui.theme.Primary
import com.courtly.coaches.ui.theme.Spacing
import com.courtly.coaches.ui.theme.TextPrimary
import com.courtly.coaches.ui.theme.TextSecondary

@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    onSignInSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var isLoginSelected by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            viewModel.consumeSuccess()
            onSignInSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = Spacing.lg,
                    vertical = Spacing.xl
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(Spacing.xxl))

            Image(
                painter = painterResource(
                    id = R.drawable.logo_courtly
                ),
                contentDescription = "Logo de Courtly",
                modifier = Modifier.size(86.dp)
            )

            Spacer(modifier = Modifier.height(Spacing.sm))

            Text(
                text = "ACCESO PARA ENTRENADORES",
                color = TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.4.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Spacing.xs))

            Text(
                text = "Courtly para\nentrenadores",
                color = TextPrimary,
                fontSize = 30.sp,
                lineHeight = 30.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Spacing.md))

            CoachAuthTabs(
                isLoginSelected = isLoginSelected,
                onLoginSelected = {
                    isLoginSelected = true
                },
                onRegisterSelected = {
                    isLoginSelected = false
                }
            )

            Spacer(modifier = Modifier.height(Spacing.md))

            if (isLoginSelected) {
                CoachLoginForm(
                    viewModel = viewModel,
                    username = uiState.username,
                    password = uiState.password,
                    isLoading = uiState.isLoading,
                    errorMessage = uiState.errorMessage
                )
            } else {
                CoachRegisterForm(
                    viewModel = viewModel,
                    uiState = uiState
                )
            }

            Spacer(modifier = Modifier.height(Spacing.md))

            Text(
                text = if (isLoginSelected) {
                    "Ingresa con una cuenta de entrenador registrada en Courtly."
                } else {
                    "Crea tu cuenta como entrenador y completa tus datos profesionales."
                },
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 15.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Spacing.lg))
        }
    }
}

@Composable
private fun CoachAuthTabs(
    isLoginSelected: Boolean,
    onLoginSelected: () -> Unit,
    onRegisterSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(
                color = androidx.compose.ui.graphics.Color(0xFFF4F8FB),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(Spacing.xs)
    ) {
        CoachAuthTabButton(
            text = "Iniciar sesión",
            isSelected = isLoginSelected,
            onClick = onLoginSelected,
            modifier = Modifier.weight(1f)
        )

        CoachAuthTabButton(
            text = "Crear cuenta",
            isSelected = !isLoginSelected,
            onClick = onRegisterSelected,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun CoachAuthTabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .minimumInteractiveComponentSize()
            .clip(RoundedCornerShape(11.dp))
            .background(
                color = if (isSelected) {
                    androidx.compose.ui.graphics.Color.White
                } else {
                    androidx.compose.ui.graphics.Color.Transparent
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = TextPrimary,
            fontSize = 13.sp,
            fontWeight = if (isSelected) {
                FontWeight.ExtraBold
            } else {
                FontWeight.SemiBold
            }
        )
    }
}

@Composable
private fun CoachLoginForm(
    viewModel: SignInViewModel,
    username: String,
    password: String,
    isLoading: Boolean,
    errorMessage: String?
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        CourtlyTextField(
            label = "Usuario o correo",
            value = username,
            onValueChange = viewModel::onUsernameChanged,
            placeholder = "Ej. coach_juan",
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.height(Spacing.sm))

        CourtlyTextField(
            label = "Contraseña",
            value = password,
            onValueChange = viewModel::onPasswordChanged,
            placeholder = "Ingresa tu contraseña",
            isPassword = true,
            imeAction = ImeAction.Done,
            onDone = viewModel::signIn
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(Spacing.sm))

            ErrorMessageBox(
                message = errorMessage
            )
        }

        Spacer(modifier = Modifier.height(Spacing.md))

        Button(
            onClick = viewModel::signIn,
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp,
                    color = DarkNavy
                )
            } else {
                Text(
                    text = "Iniciar sesión",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun CoachRegisterForm(
    viewModel: SignInViewModel,
    uiState: SignInUiState
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        CourtlyTextField(
            label = "Nombre completo",
            value = uiState.registerName,
            onValueChange = viewModel::onRegisterNameChanged,
            placeholder = "Ej. Juan Pérez",
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.height(Spacing.sm))

        CourtlyTextField(
            label = "Usuario o correo",
            value = uiState.registerUsername,
            onValueChange = viewModel::onRegisterUsernameChanged,
            placeholder = "Ej. juan@gmail.com",
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.height(Spacing.sm))

        CourtlyTextField(
            label = "Teléfono",
            value = uiState.registerPhone,
            onValueChange = viewModel::onRegisterPhoneChanged,
            placeholder = "Ej. 987654321",
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.height(Spacing.sm))

        CourtlyTextField(
            label = "Especialidad",
            value = uiState.registerExpertise,
            onValueChange = viewModel::onRegisterExpertiseChanged,
            placeholder = "Ej. Tenis, Fútbol",
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.height(Spacing.sm))

        CourtlyTextField(
            label = "Contraseña",
            value = uiState.registerPassword,
            onValueChange = viewModel::onRegisterPasswordChanged,
            placeholder = "Mínimo 6 caracteres",
            isPassword = true,
            imeAction = ImeAction.Done,
            onDone = viewModel::signUpCoach
        )

        if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(Spacing.sm))

            ErrorMessageBox(
                message = uiState.errorMessage
            )
        }

        Spacer(modifier = Modifier.height(Spacing.md))

        Button(
            onClick = viewModel::signUpCoach,
            enabled = !uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp,
                    color = DarkNavy
                )
            } else {
                Text(
                    text = "Crear cuenta",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun CourtlyTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction,
    onDone: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(Spacing.xs))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = MaterialTheme.shapes.large,
            placeholder = if (placeholder.isNotEmpty()) {
                { Text(text = placeholder, color = TextSecondary.copy(alpha = 0.6f), fontSize = 14.sp) }
            } else null,
            visualTransformation = if (isPassword) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onDone()
                }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary,
                unfocusedBorderColor = Border,
                focusedContainerColor = androidx.compose.ui.graphics.Color(0xFFF4F8FB),
                unfocusedContainerColor = androidx.compose.ui.graphics.Color(0xFFF4F8FB),
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor = Primary
            )
        )
    }
}

@Composable
private fun ErrorMessageBox(
    message: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(androidx.compose.ui.graphics.Color(0xFFFFECEC))
            .padding(Spacing.sm),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}