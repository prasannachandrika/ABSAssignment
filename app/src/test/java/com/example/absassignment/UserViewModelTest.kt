package com.example.absassignment.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.absassignment.data.data.Dob
import com.example.absassignment.data.data.Location
import com.example.absassignment.data.data.Login
import com.example.absassignment.data.data.Name
import com.example.absassignment.data.data.Picture
import com.example.absassignment.data.data.Street
import com.example.absassignment.data.data.User
import com.example.absassignment.domainn.FetchUsersUseCase
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserViewModelTest {

    // Use the InstantTaskExecutorRule to allow LiveData to work synchronously
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: UserViewModel
    private val fetchUsersUseCase: FetchUsersUseCase = mockk()

    // Use a dispatcher for coroutine testing
    private val dispatcher = StandardTestDispatcher()
    private val testScope = TestCoroutineScope(dispatcher)
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
        Dispatchers.setMain(dispatcher) // Set the Main dispatcher to the test dispatcher
        viewModel = UserViewModel(fetchUsersUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the Main dispatcher
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun `loadMoreUsers should emit Loading and then Success when users are fetched`() = runBlocking {
        // Given
        // Sample users
        coEvery { fetchUsersUseCase(20) } returns flow {
            emit(ResultState.Loading)
            emit(ResultState.Success(mockUsers))
        }

        // When
        viewModel.loadMoreUsers()

        // Then
        assert(viewModel.userState.value is ResultState.Loading) // Check for loading state
        dispatcher.scheduler.advanceUntilIdle() // Advance until idle
        assert(viewModel.userState.value is ResultState.Success<*>)
        val successState = viewModel.userState.value as ResultState.Success<*>
        assert(successState.data == mockUsers) // Assert the fetched users
    }

    @Test
    fun `loadMoreUsers should emit Loading and then Error when fetching users fails`() = runBlocking {
        // Given
        coEvery { fetchUsersUseCase(20) } returns flow {
            emit(ResultState.Loading)
            emit(ResultState.Error("An error occurred: No users found."))
        }

        // When
        viewModel.loadMoreUsers()

        // Then
        assert(viewModel.userState.value is ResultState.Loading) // Check for loading state
        dispatcher.scheduler.advanceUntilIdle() // Advance until idle
        assert(viewModel.userState.value is ResultState.Error)
        val errorState = viewModel.userState.value as ResultState.Error
        assert(errorState.message == "An error occurred: No users found.") // Assert error message
    }

    @Test
    fun `loadMoreUsers should return already loaded users if data is already loaded`() = runBlocking {
        // Given

        coEvery { fetchUsersUseCase(20) } returns flow {
            emit(ResultState.Success(mockUsers))
        }

        viewModel.loadMoreUsers() // Load users first time

        // When
        viewModel.loadMoreUsers() // Try loading again

        // Then
        assert(viewModel.userState.value is ResultState.Success<*>)
        val successState = viewModel.userState.value as ResultState.Success<*>
        assert(successState.data == mockUsers) // Assert the fetched users
        verify(exactly = 1) { fetchUsersUseCase(20) } // Verify that the use case was called only once
    }
}
