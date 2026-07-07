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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import com.courtly.coaches.ui.theme.Background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.courtly.coaches.ui.theme.Spacing
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Primary
                )
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
                    reviews = uiState.reviews,
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
}

@Composable
private fun CoachProfileContent(
    coach: Coach,
    reviews: List<com.courtly.coaches.contexts.reviews.domain.model.Review>,
    onEditProfile: () -> Unit,
    onDeleteProfile: () -> Unit,
    onSignOut: () -> Unit,
    isDeleting: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Spacing.md)
    ) {
        Text(
            text = "PERFIL DEL ENTRENADOR",
            color = TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.4.sp
        )

        Spacer(modifier = Modifier.height(Spacing.xs))

        Text(
            text = "Mi perfil",
            color = TextPrimary,
            fontSize = 30.sp,
            fontWeight = FontWeight.Black
        )

        Spacer(modifier = Modifier.height(Spacing.md))

        CoachSummaryCard(
            coach = coach,
            reviewsCount = reviews.size
        )

        Spacer(modifier = Modifier.height(Spacing.md))

        CoachActionsCard(
            onEditProfile = onEditProfile,
            onDeleteProfile = onDeleteProfile,
            isDeleting = isDeleting
        )

        Spacer(modifier = Modifier.height(Spacing.md))

        CoachReviewsSection(reviews = reviews)

        Spacer(modifier = Modifier.height(Spacing.md))

        SessionCard(
            onSignOut = onSignOut
        )
    }
}

@Composable
private fun CoachSummaryCard(
    coach: Coach,
    reviewsCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Navy,
                shape = MaterialTheme.shapes.extraLarge
            )
            .padding(Spacing.md)
    ) {
        // Profile image & basic details
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageModifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .border(width = 3.dp, color = Primary, shape = CircleShape)

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

            Spacer(modifier = Modifier.width(Spacing.md))

            Column {
                Text(
                    text = "ENTRENADOR CERTIFICADO",
                    color = Primary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.3.sp
                )

                Spacer(modifier = Modifier.height(Spacing.xs))

                Text(
                    text = coach.name,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        Spacer(modifier = Modifier.height(Spacing.md))

        // Coach statistics row
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.sm),
            modifier = Modifier.fillMaxWidth()
        ) {
            CoachStatItem(
                value = "★ 4.8",
                label = "$reviewsCount reseñas",
                modifier = Modifier.weight(1f)
            )
            CoachStatItem(
                value = "32h",
                label = "Horas dadas",
                modifier = Modifier.weight(1f)
            )
            CoachStatItem(
                value = "8",
                label = "Alumnos",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(Spacing.md))

        CoachDataBox(
            label = "ESPECIALIDAD",
            value = coach.expertise
        )

        Spacer(modifier = Modifier.height(Spacing.sm))

        CoachDataBox(
            label = "TELÉFONO",
            value = coach.phone
        )

        Spacer(modifier = Modifier.height(Spacing.sm))

        CoachDataBox(
            label = "USUARIO ID",
            value = "#${coach.userId}"
        )
    }
}

@Composable
private fun CoachStatItem(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = Color.White.copy(alpha = 0.08f),
                shape = MaterialTheme.shapes.medium
            )
            .padding(vertical = Spacing.sm, horizontal = Spacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(Spacing.xs))
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.72f),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
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
                color = Color.White.copy(alpha = 0.12f),
                shape = MaterialTheme.shapes.medium
            )
            .padding(Spacing.sm)
    ) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.55f),
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = value,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
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
                shape = MaterialTheme.shapes.extraLarge
            )
            .border(1.dp, Border, MaterialTheme.shapes.extraLarge)
            .padding(Spacing.md)
    ) {
        Text(
            text = "Gestionar perfil",
            color = TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(Spacing.xs))

        Text(
            text = "Puedes actualizar tu nombre, especialidad y teléfono.",
            color = TextSecondary,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(Spacing.md))

        Button(
            onClick = onEditProfile,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Editar perfil")
        }

        Spacer(modifier = Modifier.height(Spacing.sm))

        OutlinedButton(
            onClick = onDeleteProfile,
            enabled = !isDeleting,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            if (isDeleting) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    color = Primary,
                    modifier = Modifier.size(20.dp)
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
                shape = MaterialTheme.shapes.extraLarge
            )
            .border(1.dp, Border, MaterialTheme.shapes.extraLarge)
            .padding(Spacing.md)
    ) {
        Text(
            text = "Sesión",
            color = TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(Spacing.md))

        Button(
            onClick = onSignOut,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = MaterialTheme.shapes.medium
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
            .background(Background)
            .padding(Spacing.lg),
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

            Spacer(modifier = Modifier.height(Spacing.sm))

            Text(
                text = "Completa tus datos para comenzar a utilizar Courtly como entrenador.",
                color = TextSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Spacing.lg))

            Button(
                onClick = onCreateProfile,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Crear perfil")
            }

            Spacer(modifier = Modifier.height(Spacing.sm))

            OutlinedButton(
                onClick = onSignOut,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium
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
            .background(Background)
            .padding(Spacing.lg),
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

            Spacer(modifier = Modifier.height(Spacing.md))

            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                Button(
                    onClick = onRetry,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Reintentar")
                }

                OutlinedButton(
                    onClick = onSignOut,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Cerrar sesión")
                }
            }
        }
    }
}

@Composable
private fun CoachReviewsSection(
    reviews: List<com.courtly.coaches.contexts.reviews.domain.model.Review>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Card,
                shape = MaterialTheme.shapes.extraLarge
            )
            .border(width = 1.dp, color = Border, shape = MaterialTheme.shapes.extraLarge)
            .padding(Spacing.md)
    ) {
        Text(
            text = "RESEÑAS Y VALORACIONES",
            color = Primary,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.3.sp
        )

        Spacer(modifier = Modifier.height(Spacing.sm))

        if (reviews.isEmpty()) {
            Text(
                text = "Aún no tienes valoraciones de jugadores.",
                color = TextSecondary,
                fontSize = 14.sp
            )
        } else {
            val avgRating = reviews.map { it.score }.average()
            val formattedAvg = String.format(java.util.Locale.US, "%.1f", avgRating)
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "★ $formattedAvg",
                    color = Primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(Spacing.sm))
                Text(
                    text = "(${reviews.size} valoraciones)",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(Spacing.md))

            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.sm)
            ) {
                reviews.forEach { review ->
                    ReviewItem(review = review)
                }
            }
        }
    }
}

@Composable
private fun ReviewItem(
    review: com.courtly.coaches.contexts.reviews.domain.model.Review
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Background,
                shape = MaterialTheme.shapes.large
            )
            .padding(Spacing.sm)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = review.userName,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = TextPrimary
            )
            Text(
                text = "★ ${review.score}",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Primary
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = review.comment,
            fontSize = 13.sp,
            color = TextSecondary
        )
    }
}