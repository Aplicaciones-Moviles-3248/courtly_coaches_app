package com.courtly.coaches.contexts.notifications.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.courtly.coaches.contexts.notifications.domain.model.Notification
import com.courtly.coaches.contexts.notifications.domain.model.NotificationType
import com.courtly.coaches.contexts.notifications.presentation.viewmodel.NotificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadNotifications()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Notificaciones")
                        if (uiState.unreadCount > 0) {
                            Badge(modifier = Modifier.padding(start = 8.dp)) {
                                Text("${uiState.unreadCount}")
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }
                uiState.errorMessage != null -> {
                    Text(
                        text = uiState.errorMessage ?: "Ocurrió un error inesperado",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                uiState.notifications.isEmpty() -> {
                    Text(
                        text = "No tienes notificaciones registradas.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.notifications) { notification ->
                            NotificationItem(
                                notification = notification,
                                onItemClick = {
                                    if (!notification.isRead) {
                                        viewModel.markAsRead(notification.id)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(
    notification: Notification,
    onItemClick: () -> Unit
) {
    val backgroundColor = if (notification.isRead) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
    }

    val (icon, iconColor) = getNotificationVisuals(notification.type)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconColor.copy(alpha = 0.15f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconColor)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = notification.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (!notification.isRead) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}

private fun getNotificationVisuals(type: NotificationType): Pair<ImageVector, Color> {
    return when (type) {
        NotificationType.BOOKING_CREATED,
        NotificationType.BOOKING_CONFIRMED -> Pair(Icons.Default.DateRange, Color(0xFF4CAF50))
        NotificationType.BOOKING_CANCELLED -> Pair(Icons.Default.Delete, Color(0xFFF44336))
        NotificationType.TRAINING_SESSION_REQUESTED -> Pair(Icons.Default.AccountCircle, Color(0xFF2196F3))
        NotificationType.TRAINING_SESSION_ACCEPTED -> Pair(Icons.Default.CheckCircle, Color(0xFF4CAF50))
        NotificationType.TRAINING_SESSION_REJECTED,
        NotificationType.TRAINING_SESSION_CANCELLED -> Pair(Icons.Default.Close, Color(0xFFF44336))
        NotificationType.PAYMENT_CONFIRMED -> Pair(Icons.Default.ShoppingCart, Color(0xFFFF9800))
        NotificationType.REVIEW_ENABLED,
        NotificationType.REVIEW_SUBMITTED,
        NotificationType.REVIEW_RECEIVED -> Pair(Icons.Default.Star, Color(0xFFFFE500))
        NotificationType.MATCH_CREATED,
        NotificationType.MATCH_JOINED,
        NotificationType.MATCH_PARTICIPANT_JOINED -> Pair(Icons.Default.PlayArrow, Color(0xFF9C27B0))
        NotificationType.MATCH_CANCELLED -> Pair(Icons.Default.ExitToApp, Color(0xFF757575))
    }
}