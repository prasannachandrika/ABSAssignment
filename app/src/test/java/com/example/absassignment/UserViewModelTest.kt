package com.example.absassignment.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.absassignment.data.models.Dob
import com.example.absassignment.data.models.Location
import com.example.absassignment.data.models.Login
import com.example.absassignment.data.models.Name
import com.example.absassignment.data.models.Picture
import com.example.absassignment.data.models.Street
import com.example.absassignment.data.models.User
import com.example.absassignment.domainn.FetchUsersUseCase
import io.mockk.coEvery
import io.mockk.mockk
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

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: UserViewModel
    private val fetchUsersUseCase: FetchUsersUseCase = mockk()

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
        Dispatchers.setMain(dispatcher)
        viewModel = UserViewModel(fetchUsersUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun `loadMoreUsers should emit Loading and then Success when users are fetched`() = runBlocking {
        // Given
        coEvery { fetchUsersUseCase(20) } returns flow {
            emit(ResultState.Loading)
            emit(ResultState.Success(mockUsers))
        }

        // When
        viewModel.loadMoreUsers()

        // Then
        assert(viewModel.userState.value is ResultState.Loading)
        dispatcher.scheduler.advanceUntilIdle()
        assert(viewModel.userState.value is ResultState.Success<*>)
        val successState = viewModel.userState.value as ResultState.Success<*>
        assert(successState.data == mockUsers)
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
        assert(viewModel.userState.value is ResultState.Loading)
        dispatcher.scheduler.advanceUntilIdle()
        assert(viewModel.userState.value is ResultState.Error)
        val errorState = viewModel.userState.value as ResultState.Error
        assert(errorState.message == "An error occurred: No users found.")
    }



    @Test
    fun `loadMoreUsers should handle empty result set correctly`() = runBlocking {
        // Given
        coEvery { fetchUsersUseCase(20) } returns flow {
            emit(ResultState.Loading)
            emit(ResultState.Success(emptyList<User>()))
        }

        // When
        viewModel.loadMoreUsers()

        // Then
        assert(viewModel.userState.value is ResultState.Loading)
        dispatcher.scheduler.advanceUntilIdle()
        assert(viewModel.userState.value is ResultState.Success<*>)

        // Explicitly cast the success state to ResultState.Success<List<User>>
        val successState = viewModel.userState.value as ResultState.Success<List<User>>

        // Now call isEmpty on the List<User>
        assert(successState.data.isEmpty()) // Check that no users are returned
    }



}


