package com.courtly.coaches.contexts.notifications.infrastructure.model

import com.google.gson.annotations.SerializedName

data class NotificationCountDto (
    @SerializedName("userId") val coachId: Long,
    val unreadCount: Long
)