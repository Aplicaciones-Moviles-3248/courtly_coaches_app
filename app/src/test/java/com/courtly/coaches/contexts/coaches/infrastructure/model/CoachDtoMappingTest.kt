package com.courtly.coaches.contexts.coaches.infrastructure.model

import com.courtly.coaches.contexts.coaches.domain.model.CreateCoachParams
import com.courtly.coaches.contexts.coaches.domain.model.UpdateCoachParams
import org.junit.Assert.assertEquals
import org.junit.Test

class CoachDtoMappingTest {

    @Test
    fun `toDomain mapea todos los campos del dto`() {
        val dto = CoachDto(
            id = 4L,
            name = "Fabricio Ruiz",
            expertise = "Tenis",
            phone = "999111222",
            userId = 10L
        )

        val coach = dto.toDomain()

        assertEquals(4L, coach.id)
        assertEquals("Fabricio Ruiz", coach.name)
        assertEquals("Tenis", coach.expertise)
        assertEquals("999111222", coach.phone)
        assertEquals(10L, coach.userId)
    }

    @Test
    fun `CreateCoachRequest fromDomain copia los campos de CreateCoachParams`() {
        val params = CreateCoachParams(
            name = "Ana Lopez",
            expertise = "Futbol",
            phone = "999888777",
            userId = 3L
        )

        val request = CreateCoachRequest.fromDomain(params)

        assertEquals("Ana Lopez", request.name)
        assertEquals("Futbol", request.expertise)
        assertEquals("999888777", request.phone)
        assertEquals(3L, request.userId)
    }

    @Test
    fun `UpdateCoachRequest fromDomain copia los campos de UpdateCoachParams`() {
        val params = UpdateCoachParams(
            name = "Ana Lopez",
            expertise = "Voley",
            phone = "999888777"
        )

        val request = UpdateCoachRequest.fromDomain(params)

        assertEquals("Ana Lopez", request.name)
        assertEquals("Voley", request.expertise)
        assertEquals("999888777", request.phone)
    }
}
