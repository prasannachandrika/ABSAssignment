package com.example.absassignment.domain.usecase

import com.example.absassignment.data.models.Dob
import com.example.absassignment.data.models.Location
import com.example.absassignment.data.models.Login
import com.example.absassignment.data.models.Name
import com.example.absassignment.data.models.Picture
import com.example.absassignment.data.models.Street
import com.example.absassignment.data.models.User
import com.example.absassignment.domainn.FetchUsersUseCase

import com.example.absassignment.repository.UserRepositoryImpl
import com.example.absassignment.viewmodel.ResultState
import io.mockk.coEvery // For setting up mock behavior for suspend functions
import io.mockk.mockk // For creating mocks
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
class FetchUsersUseCaseTest {
    private lateinit var fetchUsersUseCase: FetchUsersUseCase
    private lateinit var userRepositoryImpl: UserRepositoryImpl
    // Sample user response
    private val mockUsers = listOf(
        User(
            gender = "",
            name = Name("Mrs", "Prasanna", "Yadalapurapu"),
            location = Location(Street(1, "ds"), "", "", ""),
            email = "",
            dob = Dob("", 0),
            phone = "123-456-7890",
            picture = Picture("", "", ""),
            login = Login("")
        )
    )

    @Before
    fun setUp() {
        userRepositoryImpl = mockk() // Create a mock instance of UserRepository
        fetchUsersUseCase = FetchUsersUseCase(userRepositoryImpl)
    }

    @Test
    fun `invoke should emit Loading state`() = runBlocking {
        // Arrange
        val numberOfRecords = 10

        // Act
        val flow = fetchUsersUseCase(numberOfRecords)

        // Assert
        flow.take(1).collect { resultState ->
            assertTrue(resultState is ResultState.Loading)
        }
    }

    @Test
    fun `invoke should emit Success state with users`() = runBlocking {
        // Arrange
        val numberOfRecords = 10
        coEvery { userRepositoryImpl.getUsers(numberOfRecords) } returns mockUsers

        // Act
        val flow = fetchUsersUseCase(numberOfRecords)

        // Assert
        flow.collectIndexed { index, resultState ->
            when (index) {
                0 -> {
                    assertTrue(resultState is ResultState.Loading)
                }
                1 -> {
                    assertTrue(resultState is ResultState.Success)
                    assertEquals(mockUsers, (resultState as ResultState.Success).data)
                }
                else -> {
                    fail("Expected only two emissions (Loading and Success), got more.")
                }
            }
        }
    }







    @Test
    fun `invoke should emit Error state with exception message`() = runBlocking {
        // Arrange
        val numberOfRecords = 10
        val exceptionMessage = "Network error"
        coEvery { userRepositoryImpl.getUsers(numberOfRecords) } throws RuntimeException(exceptionMessage)

        // Act
        val flow = fetchUsersUseCase(numberOfRecords)

        // Assert
        val states = mutableListOf<ResultState<List<User>>>()
        flow.take(2).collect { resultState ->
            states.add(resultState)
        }

        // Check if the first emitted state is Loading
        assertTrue(states[0] is ResultState.Loading)

        // Check if the second emitted state is the expected Error state
        assertTrue(states[1] is ResultState.Error)
        assertEquals("An error occurred: $exceptionMessage", (states[1] as ResultState.Error).message)
    }

}
