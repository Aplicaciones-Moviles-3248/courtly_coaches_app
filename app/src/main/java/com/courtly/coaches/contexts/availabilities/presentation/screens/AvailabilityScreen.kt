package com.courtly.coaches.contexts.availabilities.presentation.screens

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.courtly.coaches.contexts.availabilities.domain.model.Availability
import com.courtly.coaches.contexts.availabilities.domain.model.AvailabilityStatus
import com.courtly.coaches.contexts.availabilities.presentation.viewmodel.AvailabilityViewModel
import com.courtly.coaches.ui.theme.Background
import com.courtly.coaches.ui.theme.DarkNavy
import com.courtly.coaches.ui.theme.Primary
import com.courtly.coaches.ui.theme.TextPrimary
import com.courtly.coaches.ui.theme.TextSecondary

@Composable
fun AvailabilityScreen(
    viewModel: AvailabilityViewModel,
    coachId: Long
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMyAvailabilities()
    }

    val total = uiState.availabilities.size
    val availableCount = uiState.availabilities.count {
        it.status == AvailabilityStatus.AVAILABLE
    }
    val reservedCount = uiState.availabilities.count {
        it.status == AvailabilityStatus.RESERVED
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

            else -> {
                AvailabilityContent(
                    uiState = uiState,
                    total = total,
                    availableCount = availableCount,
                    reservedCount = reservedCount,
                    onRefresh = viewModel::loadMyAvailabilities,
                    onAddAvailability = { viewModel.clearSelectedAvailability() },
                    onEditAvailability = { viewModel.loadAvailabilityById(it.id) },
                    onDeleteAvailability = { viewModel.deleteAvailability(it.id) },
                    onSaveAvailability = { availabilityId, date, startTime, endTime, status ->
                        if (availabilityId == null) {
                            viewModel.createAvailability(
                                date = date,
                                startTime = startTime,
                                endTime = endTime,
                                status = status,
                                coachId = coachId
                            )
                        } else {
                            viewModel.updateAvailability(
                                id = availabilityId,
                                date = date,
                                startTime = startTime,
                                endTime = endTime,
                                status = status
                            )
                        }
                    },
                    onDismissDialog = viewModel::clearSelectedAvailability
                )
            }
        }
    }
}

@Composable
private fun AvailabilityContent(
    uiState: com.courtly.coaches.contexts.availabilities.presentation.viewmodel.AvailabilityUiState,
    total: Int,
    availableCount: Int,
    reservedCount: Int,
    onRefresh: () -> Unit,
    onAddAvailability: () -> Unit,
    onEditAvailability: (Availability) -> Unit,
    onDeleteAvailability: (Availability) -> Unit,
    onSaveAvailability: (
        availabilityId: Long?,
        date: String,
        startTime: String,
        endTime: String,
        status: AvailabilityStatus
    ) -> Unit,
    onDismissDialog: () -> Unit
) {
    var editorOpen by remember { mutableStateOf(false) }
    val editorAvailability = uiState.selectedAvailability

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
    ) {
        item {
            HeroCard(
                total = total,
                availableCount = availableCount,
                reservedCount = reservedCount
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onRefresh,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Actualizar")
                }

                OutlinedButton(
                    onClick = {
                        editorOpen = true
                        onAddAvailability()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Nuevo horario")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.errorMessage != null) {
                ErrorCard(message = uiState.errorMessage.orEmpty())
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        if (uiState.availabilities.isEmpty()) {
            item {
                EmptyStateCard(
                    onCreate = {
                        editorOpen = true
                        onAddAvailability()
                    }
                )
            }
        } else {
            item {
                Text(
                    text = "Tus horarios",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            items(
                items = uiState.availabilities,
                key = { it.id }
            ) { availability ->
                AvailabilityCard(
                    availability = availability,
                    onEdit = {
                        editorOpen = true
                        onEditAvailability(availability)
                    },
                    onDelete = { onDeleteAvailability(availability) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }

    if (editorOpen || editorAvailability != null) {
        AvailabilityEditorDialog(
            availability = editorAvailability,
            isSaving = uiState.isSaving,
            onDismiss = {
                editorOpen = false
                onDismissDialog()
            },
            onSave = { date, startTime, endTime, status ->
                onSaveAvailability(
                    editorAvailability?.id,
                    date,
                    startTime,
                    endTime,
                    status
                )
                editorOpen = false
            }
        )
    }
}

@Composable
private fun HeroCard(
    total: Int,
    availableCount: Int,
    reservedCount: Int
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = DarkNavy
        ),
        shape = RoundedCornerShape(28.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color.White.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Agenda del entrenador",
                        tint = Primary,
                        modifier = Modifier.padding(10.dp)
                    )
                }

                Spacer(modifier = Modifier.size(12.dp))

                Column {
                    Text(
                        text = "AVAILABILITIES",
                        color = Primary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.4.sp
                    )

                    Text(
                        text = "Agenda del entrenador",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Gestiona tus bloques horarios. Los jugadores verán aquí tu disponibilidad para reservar sesiones.",
                color = Color.White.copy(alpha = 0.82f),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                SummaryChip(
                    title = "Total",
                    value = total.toString(),
                    modifier = Modifier.weight(1f)
                )
                SummaryChip(
                    title = "Disponibles",
                    value = availableCount.toString(),
                    modifier = Modifier.weight(1f)
                )
                SummaryChip(
                    title = "Reservados",
                    value = reservedCount.toString(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SummaryChip(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(
                color = Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(18.dp)
            )
            .padding(vertical = 12.dp, horizontal = 10.dp)
    ) {
        Text(
            text = title,
            color = Color.White.copy(alpha = 0.72f),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun AvailabilityCard(
    availability: Availability,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatusDot(status = availability.status)

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${availability.date} | ${availability.startTime} - ${availability.endTime}",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = availability.status.label,
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }

            if (availability.coach != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Coach: ${availability.coach.name}",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Editar")
                }

                Button(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Eliminar")
                }
            }
        }
    }
}

@Composable
private fun StatusDot(
    status: AvailabilityStatus
) {
    val color = when (status) {
        AvailabilityStatus.AVAILABLE -> Primary
        AvailabilityStatus.RESERVED -> Color(0xFFF59E0B)
        AvailabilityStatus.UNAVAILABLE -> Color(0xFF94A3B8)
    }

    Box(
        modifier = Modifier
            .size(12.dp)
            .semantics { contentDescription = status.label }
            .background(color = color, shape = RoundedCornerShape(50))
    )
}

@Composable
private fun ErrorCard(
    message: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFECEC)),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = message,
            color = DarkNavy,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        )
    }
}

@Composable
private fun EmptyStateCard(
    onCreate: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = "Sin horarios cargados",
                tint = Primary,
                modifier = Modifier.size(42.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Todavía no has cargado horarios",
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Crea tu primer bloque para que los jugadores puedan verlo en Courtly.",
                color = TextSecondary,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(onClick = onCreate) {
                Text("Crear horario")
            }
        }
    }
}

@Composable
private fun AvailabilityEditorDialog(
    availability: Availability?,
    isSaving: Boolean,
    onDismiss: () -> Unit,
    onSave: (date: String, startTime: String, endTime: String, status: AvailabilityStatus) -> Unit
) {
    var date by remember(availability?.id) { mutableStateOf(availability?.date ?: "") }
    var startTime by remember(availability?.id) { mutableStateOf(availability?.startTime ?: "") }
    var endTime by remember(availability?.id) { mutableStateOf(availability?.endTime ?: "") }
    var status by remember(availability?.id) {
        mutableStateOf(availability?.status ?: AvailabilityStatus.AVAILABLE)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = { onSave(date, startTime, endTime, status) },
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = DarkNavy
                    )
                } else {
                    Text("Guardar")
                }
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                enabled = !isSaving
            ) {
                Text("Cancelar")
            }
        },
        title = {
            Text(
                text = if (availability == null) {
                    "Nuevo horario"
                } else {
                    "Editar horario"
                }
            )
        },
        text = {
            Column {
                Text(
                    text = "Completa tu bloque horario. Usa formato 24h para evitar errores.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Fecha") },
                    placeholder = { Text("YYYY-MM-DD") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    label = { Text("Hora inicio") },
                    placeholder = { Text("HH:mm") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = endTime,
                    onValueChange = { endTime = it },
                    label = { Text("Hora fin") },
                    placeholder = { Text("HH:mm") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Estado",
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AvailabilityStatus.values().forEach { item ->
                        val selected = status == item
                        val bg = if (selected) {
                            when (item) {
                                AvailabilityStatus.AVAILABLE -> Color(0xFFE7FFF5)
                                AvailabilityStatus.RESERVED -> Color(0xFFFFF7E5)
                                AvailabilityStatus.UNAVAILABLE -> Color(0xFFF1F5F9)
                            }
                        } else {
                            Color(0xFFF8FAFC)
                        }

                        Surface(
                            color = bg,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item.label,
                                    color = if (selected) TextPrimary else TextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}
