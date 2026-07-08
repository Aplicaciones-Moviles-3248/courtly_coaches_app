package com.courtly.coaches.contexts.notifications.domain.model

data class Notification(
    val id: Long,
    val title: String,
    val message: String,
    val type: NotificationType,
    val isRead: Boolean,
    val relatedEntityType: String?,
    val relatedEntityId: Long?,
    val createdAt: String,
    val coachId: Long,
    val coachName: String
)