package com.courtly.coaches.contexts.coaches.presentation.viewmodel

import com.courtly.coaches.contexts.coaches.application.usecases.CreateCoachUseCase
import com.courtly.coaches.contexts.coaches.application.usecases.DeleteCoachUseCase
import com.courtly.coaches.contexts.coaches.application.usecases.GetAllCoachesUseCase
import com.courtly.coaches.contexts.coaches.application.usecases.GetMyCoachUseCase
import com.courtly.coaches.contexts.coaches.application.usecases.UpdateCoachUseCase
import com.courtly.coaches.contexts.coaches.domain.model.Coach
import com.courtly.coaches.contexts.coaches.domain.model.CreateCoachParams
import com.courtly.coaches.contexts.coaches.domain.model.UpdateCoachParams
import com.courtly.coaches.contexts.coaches.domain.repository.CoachRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

private fun defaultCoach(id: Long = 1L) = Coach(
    id = id,
    name = "Fabricio Ruiz",
    expertise = "Tenis",
    phone = "999111222",
    userId = 10L
)

private fun httpError(code: Int): HttpException {
    val body = "{}".toResponseBody("application/json".toMediaTypeOrNull())
    return HttpException(Response.error<Any>(code, body))
}

private class FakeCoachRepository : CoachRepository {
    var myCoach: Coach = defaultCoach()
    var allCoaches: List<Coach> = listOf(defaultCoach())
    var createdCoach: Coach = defaultCoach()
    var updatedCoach: Coach = defaultCoach()
    var errorOnGetMyCoach: Throwable? = null
    var errorOnCreate: Throwable? = null
    var createCoachCalled = false
    var deletedId: Long? = null

    override suspend fun getAllCoaches(): List<Coach> = allCoaches

    override suspend fun getCoachById(id: Long): Coach = defaultCoach(id)

    override suspend fun getMyCoach(): Coach {
        errorOnGetMyCoach?.let { throw it }
        return myCoach
    }

    override suspend fun createCoach(params: CreateCoachParams): Coach {
        createCoachCalled = true
        errorOnCreate?.let { throw it }
        return createdCoach
    }

    override suspend fun updateCoach(id: Long, params: UpdateCoachParams): Coach = updatedCoach

    override suspend fun deleteCoach(id: Long) {
        deletedId = id
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class CoachViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private fun buildViewModel(repository: FakeCoachRepository) = CoachViewModel(
        getMyCoachUseCase = GetMyCoachUseCase(repository),
        getAllCoachesUseCase = GetAllCoachesUseCase(repository),
        createCoachUseCase = CreateCoachUseCase(repository),
        updateCoachUseCase = UpdateCoachUseCase(repository),
        deleteCoachUseCase = DeleteCoachUseCase(repository)
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `createCoach no llama al repositorio cuando el nombre es invalido`() = runTest(testDispatcher) {
        val repository = FakeCoachRepository()
        val viewModel = buildViewModel(repository)

        viewModel.createCoach(name = "Al", expertise = "Tenis", phone = "999111222", userId = 1)

        assertEquals("Ingresa un nombre válido.", viewModel.uiState.value.errorMessage)
        assertFalse(repository.createCoachCalled)
    }

    @Test
    fun `createCoach recorta espacios y guarda el coach creado`() = runTest(testDispatcher) {
        val repository = FakeCoachRepository().apply {
            createdCoach = defaultCoach(id = 5L)
        }
        val viewModel = buildViewModel(repository)

        viewModel.createCoach(name = "  Ana Lopez  ", expertise = " Futbol ", phone = " 999888777 ", userId = 3)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(repository.createCoachCalled)
        assertEquals(5L, viewModel.uiState.value.coach?.id)
        assertTrue(viewModel.uiState.value.operationSuccess)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `createCoach mapea 409 a mensaje de perfil duplicado`() = runTest(testDispatcher) {
        val repository = FakeCoachRepository().apply {
            errorOnCreate = httpError(409)
        }
        val viewModel = buildViewModel(repository)

        viewModel.createCoach(name = "Ana Lopez", expertise = "Futbol", phone = "999888777", userId = 3)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Ya existe un perfil asociado a este usuario.", viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isSaving)
    }

    @Test
    fun `loadMyCoach marca profileNotFound cuando el backend responde 404`() = runTest(testDispatcher) {
        val repository = FakeCoachRepository().apply {
            errorOnGetMyCoach = httpError(404)
        }
        val viewModel = buildViewModel(repository)

        viewModel.loadMyCoach()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.uiState.value.profileNotFound)
        assertNull(viewModel.uiState.value.coach)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `loadMyCoach carga el coach cuando el backend responde correctamente`() = runTest(testDispatcher) {
        val repository = FakeCoachRepository().apply {
            myCoach = defaultCoach(id = 7L)
        }
        val viewModel = buildViewModel(repository)

        viewModel.loadMyCoach()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(7L, viewModel.uiState.value.coach?.id)
        assertFalse(viewModel.uiState.value.profileNotFound)
    }

    @Test
    fun `deleteCoach limpia el coach y marca profileNotFound`() = runTest(testDispatcher) {
        val repository = FakeCoachRepository()
        val viewModel = buildViewModel(repository)

        viewModel.deleteCoach(9L)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(9L, repository.deletedId)
        assertNull(viewModel.uiState.value.coach)
        assertTrue(viewModel.uiState.value.profileNotFound)
        assertTrue(viewModel.uiState.value.operationSuccess)
    }
}
