package com.example.rickandmortyapi

import com.example.rickandmortyapi.API.CharacterResponse
import com.example.rickandmortyapi.API.Info
import com.example.rickandmortyapi.API.RickAndMortyApiService
import com.example.rickandmortyapi.DataClasses.Character
import com.example.rickandmortyapi.DataClasses.Origin
import com.example.rickandmortyapi.DataClasses.Location
import com.example.rickandmortyapi.ViewModel.CharacterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
// я сдаюсь
@ExperimentalCoroutinesApi
class CharacterViewModelTest {

    private lateinit var viewModel: CharacterViewModel
    private lateinit var mockApiService: RickAndMortyApiService
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        // Устанавливаем тестовый диспетчер
        Dispatchers.setMain(testDispatcher)

        // Инициализируем мок
        mockApiService = mock(RickAndMortyApiService::class.java)

        // Создаем ViewModel и передаем мок
        viewModel = CharacterViewModel()
        viewModel.setApiService(mockApiService)
    }

    @After
    fun tearDown() {
        // Восстанавливаем оригинальный диспетчер
        Dispatchers.resetMain()
    }

    @Test
    fun `loadCharacters should update characters LiveData`() = runTest {
        // Подготовка данных
        val expectedCharacters = listOf(
            Character(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                origin = Origin("Earth", "https://rickandmortyapi.com/api/location/1"),
                location = Location("Earth", "https://rickandmortyapi.com/api/location/20"),
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                episode = listOf(),
                url = "https://rickandmortyapi.com/api/character/1",
                created = "2017-11-04T18:48:46.250Z"
            )
        )
        val response = CharacterResponse(info = Info(1, 1, null, null), results = expectedCharacters)

        // Симулируем успешный ответ от API
        `when`(mockApiService.getCharacters()).thenReturn(response)

        // Запускаем загрузку данных
        viewModel.loadCharacters()

        // Даем корутине время на выполнение
        advanceUntilIdle()

        // Проверяем, что данные обновились
        assertEquals(expectedCharacters, viewModel.characters.value)
    }

    @Test
    fun `loadCharacters should update errorMessage on failure`() = runTest {
        // Симулируем ошибку сети
        `when`(mockApiService.getCharacters()).thenThrow(RuntimeException("Timeout"))

        // Запускаем загрузку данных
        viewModel.loadCharacters()

        // Даем корутине время на выполнение
        advanceUntilIdle()

        // Проверяем, что ошибка обновилась
        assertEquals("Ошибка сети: Timeout", viewModel.errorMessage.value)
    }
}