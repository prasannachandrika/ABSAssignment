package com.example.absassignment.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.absassignment.data.model.User
import com.example.absassignment.data.model.Name
import com.example.absassignment.data.model.Location
import com.example.absassignment.data.model.Dob
import com.example.absassignment.data.model.Picture
import com.example.absassignment.data.model.Login
import com.example.absassignment.data.model.Street
import com.example.absassignment.repository.UserRepository
import com.example.absassignment.viewmodel.ResultState.*
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: UserViewModel
    private val repository: UserRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    private val userObserver: Observer<ResultState<List<User>>> = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = UserViewModel(repository)
        viewModel.userState.observeForever(userObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchUsers should emit Loading state initially`() = runTest {
        // Arrange
        coEvery { repository.getUsers(any()) } returns emptyList()

        // Act
        viewModel.fetchUsers(10)
        advanceUntilIdle()

        // Assert
        verify { userObserver.onChanged(Loading) }
    }

    @Test
    fun `fetchUsers should emit Success state with data when repository returns a list of users`() = runTest {
        // Arrange: Mock a list of users with only `name` and `phone` initialized
        val mockUsers = listOf(
            User(
                gender = "",
                name = Name("Mrs","Prasanna","Yadalapurapu"),
                location = Location(Street(1,"ds"), "", "", "",), // Provide empty strings or defaults for unused fields
                email = "",
                dob = Dob("", 0),
                phone = "123-456-7890",
                picture = Picture("","",""),
                login = Login("")
            )
        )
        coEvery { repository.getUsers(any()) } returns mockUsers

        // Act
        viewModel.fetchUsers(10)
        advanceUntilIdle()

        // Assert
        verify { userObserver.onChanged(Loading) }
        verify { userObserver.onChanged(Success(mockUsers)) }
        assertEquals(Success(mockUsers), viewModel.userState.value)
    }

    @Test
    fun `fetchUsers should emit Error state when repository returns null`() = runTest {
        // Arrange
        coEvery { repository.getUsers(any()) } returns null

        // Act
        viewModel.fetchUsers(10)
        advanceUntilIdle()

        // Assert
        verify { userObserver.onChanged(Loading) }
        verify { userObserver.onChanged(Error("Failed to fetch users. Please try again.")) }
        assertEquals(Error("Failed to fetch users. Please try again."), viewModel.userState.value)
    }

    @Test
    fun `fetchUsers should emit Error state when repository throws an exception`() = runTest {
        // Arrange
        val exceptionMessage = "Network error"
        coEvery { repository.getUsers(any()) } throws Exception(exceptionMessage)

        // Act
        viewModel.fetchUsers(10)
        advanceUntilIdle()

        // Assert
        verify { userObserver.onChanged(Loading) }
        verify { userObserver.onChanged(Error("An error occurred: $exceptionMessage")) }
        assertEquals(Error("An error occurred: $exceptionMessage"), viewModel.userState.value)
    }
}
