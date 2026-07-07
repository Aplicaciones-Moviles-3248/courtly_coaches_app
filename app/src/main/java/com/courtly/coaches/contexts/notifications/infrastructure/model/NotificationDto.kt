package com.courtly.coaches.contexts.notifications.infrastructure.model

import com.courtly.coaches.contexts.notifications.domain.model.Notification
import com.courtly.coaches.contexts.notifications.domain.model.NotificationType

data class CoachSummaryDto(
    val id: Long,
    val name: String
)

data class NotificationDto(
    val id: Long,
    val title: String,
    val message: String,
    val type: NotificationType,
    val isRead: Boolean,
    val relatedEntityType: String?,
    val relatedEntityId: Long?,
    val createdAt: String,
    val coach: CoachSummaryDto
) {
    fun toDomain(): Notification {
        return Notification(
            id = id,
            title = title,
            message = message,
            type = type,
            isRead = isRead,
            relatedEntityType = relatedEntityType,
            relatedEntityId = relatedEntityId,
            createdAt = createdAt,
            coachId = coach.id,
            coachName = coach.name
        )
    }
}