package com.courtly.coaches.contexts.coaches.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.courtly.coaches.contexts.coaches.domain.model.Coach
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.courtly.coaches.R
import com.courtly.coaches.contexts.coaches.presentation.viewmodel.CoachViewModel
import com.courtly.coaches.ui.theme.Border
import com.courtly.coaches.ui.theme.Card
import com.courtly.coaches.ui.theme.Navy
import com.courtly.coaches.ui.theme.Primary
import com.courtly.coaches.ui.theme.TextPrimary
import com.courtly.coaches.ui.theme.TextSecondary

@Composable
fun CoachProfileScreen(
    viewModel: CoachViewModel,
    onCreateProfile: () -> Unit,
    onEditProfile: (Coach) -> Unit,
    onSignOut: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMyCoach()
    }

    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        uiState.profileNotFound -> {
            EmptyCoachProfileView(
                onCreateProfile = onCreateProfile,
                onSignOut = onSignOut
            )
        }

        uiState.errorMessage != null -> {
            ErrorCoachProfileView(
                message = uiState.errorMessage.orEmpty(),
                onRetry = viewModel::loadMyCoach,
                onSignOut = onSignOut
            )
        }

        uiState.coach != null -> {
            CoachProfileContent(
                coach = uiState.coach!!,
                onEditProfile = {
                    onEditProfile(uiState.coach!!)
                },
                onDeleteProfile = {
                    viewModel.deleteCoach(uiState.coach!!.id)
                },
                onSignOut = onSignOut,
                isDeleting = uiState.isDeleting
            )
        }
    }
}

@Composable
private fun CoachProfileContent(
    coach: Coach,
    onEditProfile: () -> Unit,
    onDeleteProfile: () -> Unit,
    onSignOut: () -> Unit,
    isDeleting: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = 22.dp,
                vertical = 28.dp
            )
    ) {
        Text(
            text = "PERFIL DEL ENTRENADOR",
            color = TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.4.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Mi perfil",
            color = TextPrimary,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(20.dp))

        CoachSummaryCard(coach = coach)

        Spacer(modifier = Modifier.height(18.dp))

        CoachActionsCard(
            onEditProfile = onEditProfile,
            onDeleteProfile = onDeleteProfile,
            isDeleting = isDeleting
        )

        Spacer(modifier = Modifier.height(18.dp))

        SessionCard(
            onSignOut = onSignOut
        )
    }
}

@Composable
private fun CoachSummaryCard(
    coach: Coach
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Navy,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(20.dp)
    ) {
        // Profile image
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            val imageModifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .border(width = 3.dp, color = Primary, shape = CircleShape)

            // Coach model currently has no imageUrl field; use placeholder
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(null)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_coach_placeholder),
                error = painterResource(id = R.drawable.ic_coach_placeholder),
                contentDescription = "Foto del entrenador",
                contentScale = ContentScale.Crop,
                modifier = imageModifier
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "ENTRENADOR",
            color = Primary,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.3.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = coach.name,
            color = androidx.compose.ui.graphics.Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(18.dp))

        CoachDataBox(
            label = "ESPECIALIDAD",
            value = coach.expertise
        )

        Spacer(modifier = Modifier.height(10.dp))

        CoachDataBox(
            label = "TELÉFONO",
            value = coach.phone
        )

        Spacer(modifier = Modifier.height(10.dp))

        CoachDataBox(
            label = "USUARIO",
            value = coach.userId.toString()
        )
    }
}

@Composable
private fun CoachDataBox(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.12f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(14.dp)
    ) {
        Text(
            text = label,
            color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.55f),
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            color = androidx.compose.ui.graphics.Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun CoachActionsCard(
    onEditProfile: () -> Unit,
    onDeleteProfile: () -> Unit,
    isDeleting: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Card,
                shape = RoundedCornerShape(22.dp)
            )
            .padding(18.dp)
    ) {
        Text(
            text = "Gestionar perfil",
            color = TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Puedes actualizar tu nombre, especialidad y teléfono.",
            color = TextSecondary,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = onEditProfile,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Editar perfil")
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onDeleteProfile,
            enabled = !isDeleting,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            if (isDeleting) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp
                )
            } else {
                Text("Eliminar perfil")
            }
        }
    }
}

@Composable
private fun SessionCard(
    onSignOut: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Card,
                shape = RoundedCornerShape(22.dp)
            )
            .padding(18.dp)
    ) {
        Text(
            text = "Sesión",
            color = TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = onSignOut,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Text("Cerrar sesión")
        }
    }
}

@Composable
private fun EmptyCoachProfileView(
    onCreateProfile: () -> Unit,
    onSignOut: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Aún no tienes un perfil de entrenador.",
                color = TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Completa tus datos para comenzar a utilizar Courtly como entrenador.",
                color = TextSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = onCreateProfile,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Crear perfil")
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onSignOut,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}

@Composable
private fun ErrorCoachProfileView(
    message: String,
    onRetry: () -> Unit,
    onSignOut: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = message,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onRetry
                ) {
                    Text("Reintentar")
                }

                OutlinedButton(
                    onClick = onSignOut
                ) {
                    Text("Cerrar sesión")
                }
            }
        }
    }
}