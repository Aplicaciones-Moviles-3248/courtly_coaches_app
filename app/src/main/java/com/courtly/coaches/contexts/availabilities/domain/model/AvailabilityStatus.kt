package com.courtly.coaches.contexts.availabilities.domain.model

enum class AvailabilityStatus {
    AVAILABLE,
    RESERVED,
    UNAVAILABLE;

    val label: String
        get() = when (this) {
            AVAILABLE -> "Disponible"
            RESERVED -> "Reservado"
            UNAVAILABLE -> "No disponible"
        }

    companion object {
        fun fromApiValue(value: String): AvailabilityStatus {
            return runCatching {
                valueOf(value.uppercase())
            }.getOrElse {
                AVAILABLE
            }
        }
    }
}
