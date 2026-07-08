package com.courtly.coaches.contexts.analytics.domain.model

enum class MetricType {
    SESSIONS_COMPLETED,
    BOOKINGS_RECEIVED,
    REVENUE_TOTAL,
    AVERAGE_RATING;

    val displayName: String
        get() = when(this){
            SESSIONS_COMPLETED -> "Sesiones completadas"
            BOOKINGS_RECEIVED -> "Solicitudes recibidas"
            REVENUE_TOTAL -> "Ingresos"
            AVERAGE_RATING -> "Calificación promedio"
        }

    companion object{
        fun fromApiValue(value:String): MetricType{
            return entries.firstOrNull{
                it.name == value
            } ?: SESSIONS_COMPLETED
        }
    }
}