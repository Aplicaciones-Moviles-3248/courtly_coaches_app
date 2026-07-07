package com.courtly.coaches.contexts.iam.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.courtly.coaches.R
import com.courtly.coaches.contexts.iam.presentation.viewmodel.SignInUiState
import com.courtly.coaches.contexts.iam.presentation.viewmodel.SignInViewModel
import com.courtly.coaches.ui.theme.Background
import com.courtly.coaches.ui.theme.Border
import com.courtly.coaches.ui.theme.DarkNavy
import com.courtly.coaches.ui.theme.Primary
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
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 28.dp,
                    vertical = 32.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(42.dp))

            Image(
                painter = painterResource(
                    id = R.drawable.logo_courtly
                ),
                contentDescription = "Logo de Courtly",
                modifier = Modifier.size(86.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "ACCESO PARA ENTRENADORES",
                color = TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.4.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Courtly para\nentrenadores",
                color = TextPrimary,
                fontSize = 30.sp,
                lineHeight = 30.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(22.dp))

            CoachAuthTabs(
                isLoginSelected = isLoginSelected,
                onLoginSelected = {
                    isLoginSelected = true
                },
                onRegisterSelected = {
                    isLoginSelected = false
                }
            )

            Spacer(modifier = Modifier.height(18.dp))

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

            Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.height(32.dp))
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
                color = androidx.compose.ui.graphics.Color(
                    0xFFF4F8FB
                ),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(4.dp)
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
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.height(14.dp))

        CourtlyTextField(
            label = "Contraseña",
            value = password,
            onValueChange = viewModel::onPasswordChanged,
            isPassword = true,
            imeAction = ImeAction.Done,
            onDone = viewModel::signIn
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(14.dp))

            ErrorMessageBox(
                message = errorMessage
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = viewModel::signIn,
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
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
    var showTermsDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        CourtlyTextField(
            label = "Nombre completo",
            value = uiState.registerName,
            onValueChange = viewModel::onRegisterNameChanged,
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.height(14.dp))

        CourtlyTextField(
            label = "Usuario o correo",
            value = uiState.registerUsername,
            onValueChange = viewModel::onRegisterUsernameChanged,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.height(14.dp))

        CourtlyTextField(
            label = "Teléfono",
            value = uiState.registerPhone,
            onValueChange = viewModel::onRegisterPhoneChanged,
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.height(14.dp))

        CourtlyTextField(
            label = "Especialidad",
            value = uiState.registerExpertise,
            onValueChange = viewModel::onRegisterExpertiseChanged,
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.height(14.dp))

        CourtlyTextField(
            label = "Contraseña",
            value = uiState.registerPassword,
            onValueChange = viewModel::onRegisterPasswordChanged,
            isPassword = true,
            imeAction = ImeAction.Done,
            onDone = viewModel::signUpCoach
        )

        Spacer(modifier = Modifier.height(14.dp))

        TermsAcceptanceRow(
            accepted = uiState.termsAccepted,
            onAcceptedChange = viewModel::onTermsAcceptedChanged,
            onTermsClick = {
                showTermsDialog = true
            }
        )

        if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(14.dp))

            ErrorMessageBox(
                message = uiState.errorMessage
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = viewModel::signUpCoach,
            enabled = !uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
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

    if (showTermsDialog) {
        CourtlyTermsDialog(
            onDismiss = {
                showTermsDialog = false
            },
            onAcceptTerms = {
                viewModel.onTermsAcceptedChanged(true)
                showTermsDialog = false
            }
        )
    }
}

@Composable
private fun TermsAcceptanceRow(
    accepted: Boolean,
    onAcceptedChange: (Boolean) -> Unit,
    onTermsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = accepted,
            onCheckedChange = onAcceptedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = Primary,
                uncheckedColor = Border,
                checkmarkColor = DarkNavy
            ),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.size(10.dp))

        TermsAcceptanceText(
            onTermsClick = onTermsClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TermsAcceptanceText(
    onTermsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val text = "Acepto los Términos y Condiciones de Courtly"
    val linkText = "Términos y Condiciones"
    val linkStart = text.indexOf(linkText)
    val linkEnd = linkStart + linkText.length
    var layoutResult by remember {
        mutableStateOf<TextLayoutResult?>(null)
    }
    val annotatedText = remember {
        buildAnnotatedString {
            append(text.substring(0, linkStart))
            withStyle(
                SpanStyle(
                    color = Primary,
                    fontWeight = FontWeight.ExtraBold,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(linkText)
            }
            append(text.substring(linkEnd))
        }
    }

    Text(
        text = annotatedText,
        color = TextSecondary,
        fontSize = 12.sp,
        lineHeight = 17.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures { offset ->
                val position = layoutResult?.getOffsetForPosition(offset)
                    ?: return@detectTapGestures

                if (position in linkStart until linkEnd) {
                    onTermsClick()
                }
            }
        },
        onTextLayout = {
            layoutResult = it
        }
    )
}

@Composable
private fun CourtlyTermsDialog(
    onDismiss: () -> Unit,
    onAcceptTerms: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val dialogMaxHeight = if (screenHeight < 700.dp) {
        screenHeight - 56.dp
    } else {
        620.dp
    }
    val bodyMaxHeight = if (screenHeight < 700.dp) {
        screenHeight - 220.dp
    } else {
        420.dp
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .heightIn(max = dialogMaxHeight),
            shape = RoundedCornerShape(18.dp),
            color = Color.White,
            tonalElevation = 8.dp,
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 18.dp,
                            end = 8.dp,
                            top = 12.dp,
                            bottom = 12.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Términos y Condiciones",
                        color = TextPrimary,
                        fontSize = 19.sp,
                        lineHeight = 23.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = TextSecondary
                        )
                    }
                }

                HorizontalDivider(
                    color = Border,
                    thickness = 1.dp
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = bodyMaxHeight)
                        .verticalScroll(rememberScrollState())
                        .padding(
                            horizontal = 18.dp,
                            vertical = 16.dp
                        )
                ) {
                    Text(
                        text = CourtlyTermsIntro,
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    CourtlyTermsSections.forEachIndexed { index, section ->
                        TermsSectionItem(section = section)

                        if (index < CourtlyTermsSections.lastIndex) {
                            Spacer(modifier = Modifier.height(14.dp))
                        }
                    }
                }

                HorizontalDivider(
                    color = Border,
                    thickness = 1.dp
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(
                            text = "Cerrar",
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = onAcceptTerms,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary,
                            contentColor = DarkNavy
                        )
                    ) {
                        Text(
                            text = "Aceptar términos",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TermsSectionItem(
    section: CourtlyTermsSection
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = section.title,
            color = TextPrimary,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = section.body,
            color = TextSecondary,
            fontSize = 12.5.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun LegacyCourtlyTermsDialog(
    onDismiss: () -> Unit,
    onAcceptTerms: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Términos y Condiciones",
                color = TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
        },
        text = {
            Text(
                text = CourtlyTermsAndConditionsText,
                color = TextSecondary,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 360.dp)
                    .verticalScroll(rememberScrollState())
            )
        },
        confirmButton = {
            Button(onClick = onAcceptTerms) {
                Text(
                    text = "Aceptar términos",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cerrar",
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        shape = RoundedCornerShape(22.dp)
    )
}

@Composable
private fun CourtlyTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
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

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
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
                focusedContainerColor =
                    androidx.compose.ui.graphics.Color(
                        0xFFF4F8FB
                    ),
                unfocusedContainerColor =
                    androidx.compose.ui.graphics.Color(
                        0xFFF4F8FB
                    ),
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
            .clip(
                RoundedCornerShape(14.dp)
            )
            .background(
                androidx.compose.ui.graphics.Color(
                    0xFFFFECEC
                )
            )
            .padding(14.dp),
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

private data class CourtlyTermsSection(
    val title: String,
    val body: String
)

private const val CourtlyTermsIntro =
    "Bienvenido a Courtly. Al acceder y utilizar nuestra plataforma, aceptas los presentes Términos y Condiciones. Te recomendamos leerlos atentamente antes de usar nuestros servicios."

private val CourtlyTermsSections = listOf(
    CourtlyTermsSection(
        title = "1. Aceptación de los términos",
        body = "El uso de Courtly implica la aceptación plena de estos términos. Si no estás de acuerdo con alguna de sus disposiciones, te pedimos que no utilices la plataforma."
    ),
    CourtlyTermsSection(
        title = "2. Descripción del servicio",
        body = "Courtly es una plataforma digital que permite a los usuarios buscar y reservar canchas deportivas, así como conectar con entrenadores independientes. Algunas funciones pueden incluir gestión de disponibilidad, pagos digitales, valoraciones y notificaciones relacionadas con la actividad."
    ),
    CourtlyTermsSection(
        title = "3. Uso permitido",
        body = "El usuario se compromete a utilizar Courtly de forma responsable, lícita y respetuosa. Está prohibido usar la plataforma para fines fraudulentos, para suplantar identidades, afectar la experiencia de otros usuarios o vulnerar el funcionamiento del sistema."
    ),
    CourtlyTermsSection(
        title = "4. Registro y cuenta",
        body = "Para acceder a ciertas funcionalidades, puede ser necesario crear una cuenta. El usuario es responsable de proporcionar información veraz, mantener actualizados sus datos y resguardar la confidencialidad de sus credenciales de acceso."
    ),
    CourtlyTermsSection(
        title = "5. Reservas, pagos y servicios ofrecidos",
        body = "Courtly facilita la interacción entre usuarios, canchas deportivas y entrenadores independientes. La disponibilidad, condiciones del servicio, precios y cumplimiento de las actividades ofrecidas podrán depender de cada proveedor o profesional registrado en la plataforma."
    ),
    CourtlyTermsSection(
        title = "6. Propiedad intelectual",
        body = "Todo el contenido de Courtly, incluyendo nombre comercial, diseño, interfaz, elementos visuales, funcionalidades, textos y demás materiales asociados, está protegido por la normativa aplicable sobre propiedad intelectual. No está permitido copiar, distribuir o reutilizar dicho contenido sin autorización previa."
    ),
    CourtlyTermsSection(
        title = "7. Limitación de responsabilidad",
        body = "Courtly no garantiza que la plataforma esté libre de interrupciones o errores en todo momento. Tampoco será responsable por daños indirectos, pérdida de oportunidades, conflictos entre usuarios o incumplimientos atribuibles a terceros, en la medida permitida por la legislación aplicable."
    ),
    CourtlyTermsSection(
        title = "8. Protección de datos",
        body = "El tratamiento de los datos personales se rige por nuestra Política de Privacidad. Al utilizar la plataforma, el usuario reconoce haber leído dicha política y entender cómo Courtly recopila y utiliza la información."
    ),
    CourtlyTermsSection(
        title = "9. Modificaciones",
        body = "Courtly podrá actualizar estos Términos y Condiciones cuando resulte necesario. Cualquier modificación relevante será publicada en esta página y entrará en vigor desde su publicación o desde la fecha que se indique expresamente."
    ),
    CourtlyTermsSection(
        title = "10. Legislación aplicable",
        body = "Estos términos se interpretarán de acuerdo con la legislación aplicable en la jurisdicción correspondiente a la operación de la plataforma. Cualquier controversia será resuelta por las autoridades o tribunales competentes según corresponda."
    )
)

private val CourtlyTermsAndConditionsText = """
Bienvenido a Courtly. Al acceder y utilizar nuestra plataforma, aceptas los presentes Términos y Condiciones. Te recomendamos leerlos atentamente antes de usar nuestros servicios.

1. Aceptación de los términos
El uso de Courtly implica la aceptación plena de estos términos. Si no estás de acuerdo con alguna de sus disposiciones, te pedimos que no utilices la plataforma.

2. Descripción del servicio
Courtly es una plataforma digital que permite a los usuarios buscar y reservar canchas deportivas, así como conectar con entrenadores independientes. Algunas funciones pueden incluir gestión de disponibilidad, pagos digitales, valoraciones y notificaciones relacionadas con la actividad.

3. Uso permitido
El usuario se compromete a utilizar Courtly de forma responsable, lícita y respetuosa. Está prohibido usar la plataforma para fines fraudulentos, para suplantar identidades, afectar la experiencia de otros usuarios o vulnerar el funcionamiento del sistema.

4. Registro y cuenta
Para acceder a ciertas funcionalidades, puede ser necesario crear una cuenta. El usuario es responsable de proporcionar información veraz, mantener actualizados sus datos y resguardar la confidencialidad de sus credenciales de acceso.

5. Reservas, pagos y servicios ofrecidos
Courtly facilita la interacción entre usuarios, canchas deportivas y entrenadores independientes. La disponibilidad, condiciones del servicio, precios y cumplimiento de las actividades ofrecidas podrán depender de cada proveedor o profesional registrado en la plataforma.

6. Propiedad intelectual
Todo el contenido de Courtly, incluyendo nombre comercial, diseño, interfaz, elementos visuales, funcionalidades, textos y demás materiales asociados, está protegido por la normativa aplicable sobre propiedad intelectual. No está permitido copiar, distribuir o reutilizar dicho contenido sin autorización previa.

7. Limitación de responsabilidad
Courtly no garantiza que la plataforma esté libre de interrupciones o errores en todo momento. Tampoco será responsable por daños indirectos, pérdida de oportunidades, conflictos entre usuarios o incumplimientos atribuibles a terceros, en la medida permitida por la legislación aplicable.

8. Protección de datos
El tratamiento de los datos personales se rige por nuestra Política de Privacidad. Al utilizar la plataforma, el usuario reconoce haber leído dicha política y entender cómo Courtly recopila y utiliza la información.

9. Modificaciones
Courtly podrá actualizar estos Términos y Condiciones cuando resulte necesario. Cualquier modificación relevante será publicada en esta página y entrará en vigor desde su publicación o desde la fecha que se indique expresamente.

10. Legislación aplicable
Estos términos se interpretarán de acuerdo con la legislación aplicable en la jurisdicción correspondiente a la operación de la plataforma. Cualquier controversia será resuelta por las autoridades o tribunales competentes según corresponda.
""".trimIndent()
