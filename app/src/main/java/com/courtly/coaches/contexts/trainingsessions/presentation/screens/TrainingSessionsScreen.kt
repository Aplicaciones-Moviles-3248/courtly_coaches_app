package com.courtly.coaches.contexts.trainingsessions.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.courtly.coaches.contexts.trainingsessions.domain.model.TrainingSession
import com.courtly.coaches.contexts.trainingsessions.domain.model.TrainingSessionStatus
import com.courtly.coaches.contexts.trainingsessions.presentation.viewmodel.TrainingSessionsViewModel
import com.courtly.coaches.ui.theme.*

@Composable
fun TrainingSessionsScreen(
    viewModel: TrainingSessionsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.loadMyTrainingSessions()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 22.dp, vertical = 24.dp)
    ) {
        Text(
            text = "SOLICITUDES DE ENTRENAMIENTO",
            color = TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.4.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Sesiones",
            color = TextPrimary,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(16.dp))

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = Primary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Pendientes", fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Historial", fontWeight = FontWeight.Bold) }
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Primary)
            }
        } else if (uiState.errorMessage != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = uiState.errorMessage ?: "Ocurrió un error inesperado.",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            val sessions = uiState.sessions
            val filteredSessions = if (selectedTab == 0) {
                sessions.filter { it.status == TrainingSessionStatus.PENDING }
            } else {
                sessions.filter { it.status != TrainingSessionStatus.PENDING }
            }

            if (filteredSessions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (selectedTab == 0) "No tienes solicitudes pendientes." else "No hay historial de sesiones.",
                        color = TextSecondary,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredSessions) { session ->
                        TrainingSessionCard(
                            session = session,
                            onAccept = { viewModel.acceptTrainingSession(session.id) },
                            onReject = { viewModel.rejectTrainingSession(session.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TrainingSessionCard(
    session: TrainingSession,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Card),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Border, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Cliente: ${session.playerName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextPrimary
                )
                
                StatusBadge(status = session.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Cancha: ${session.courtName}",
                fontSize = 14.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(4.dp))

            val timeParts = session.startTime.split("T")
            val date = if (timeParts.isNotEmpty()) timeParts[0] else ""
            val time = if (timeParts.size > 1) timeParts[1].substring(0, 5) else ""

            Text(
                text = "Fecha: $date a las $time",
                fontSize = 14.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Precio: S/ ${session.price}",
                fontSize = 14.sp,
                color = TextSecondary,
                fontWeight = FontWeight.SemiBold
            )

            if (session.status == TrainingSessionStatus.PENDING) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onReject,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Rechazar", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onAccept,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = DarkNavy),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Aceptar", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: TrainingSessionStatus) {
    val (color, text) = when (status) {
        TrainingSessionStatus.PENDING -> TextSecondary to "PENDIENTE"
        TrainingSessionStatus.ACCEPTED -> Primary to "ACEPTADO"
        TrainingSessionStatus.REJECTED -> Color.Red to "RECHAZADO"
        TrainingSessionStatus.CANCELLED -> Color.Gray to "CANCELADO"
        TrainingSessionStatus.COMPLETED -> Primary to "COMPLETADO"
    }

    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
