package com.example.absassignment.repository

import com.example.absassignment.data.models.Dob
import com.example.absassignment.data.models.Location
import com.example.absassignment.data.models.Login
import com.example.absassignment.data.models.Name
import com.example.absassignment.data.models.Picture
import com.example.absassignment.data.models.Street
import com.example.absassignment.data.models.User
import com.example.absassignment.data.model.UserResponse
import com.example.absassignment.data.remote.source.RemoteDataSource
import com.example.absassignment.domainn.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import retrofit2.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
class UserRepositoryImplTest {
    private lateinit var userRepository: UserRepository
    private val remoteDataSource: RemoteDataSource = mockk()
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
        userRepository = UserRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `getUsers should return users when the API call is successful`() = runBlocking {
        // Arrange
        val number = 10
        val mockUserResponse = UserResponse(mockUsers) // Wrap the users in UserResponse
        val response = Response.success(mockUserResponse) // Create success response
        coEvery { remoteDataSource.fetchRandomUsers(number) } returns response // Mock the RemoteDataSource call

        // Act
        val users = userRepository.getUsers(number)

        // Assert
        assertEquals(mockUsers, users)
        coVerify { remoteDataSource.fetchRandomUsers(number) }
    }

    @Test
    fun `getUsers should return null when the API call is unsuccessful`() = runBlocking {
        // Arrange
        val number = 10
        // Create an error response for UserResponse
        val response = Response.error<UserResponse>(404, ResponseBody.create(null, "Not Found"))
        // Mock the RemoteDataSource call to return this error response
        coEvery { remoteDataSource.fetchRandomUsers(number) } returns response

        // Act
        val users = userRepository.getUsers(number)

        // Assert
        assertNull(users)
        coVerify { remoteDataSource.fetchRandomUsers(number) }
    }

    @Test
    fun `getUsers should return null when an exception occurs`() = runBlocking {
        // Arrange
        val number = 10
        coEvery { remoteDataSource.fetchRandomUsers(number) } throws Exception("Network Error")

        // Act
        val users = userRepository.getUsers(number)

        // Assert
        assertNull(users)
        coVerify { remoteDataSource.fetchRandomUsers(number) }
    }
}
