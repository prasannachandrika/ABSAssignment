package com.example.absassignment.repository

import com.example.absassignment.data.data.User
import com.example.absassignment.data.remote.ApiService
import com.example.absassignment.domainn.UserRepository
import javax.inject.Inject

// Data Layer - UserRepository Implementation
class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : UserRepository {

    override suspend fun getUsers(number: Int): List<User>? {
       // Log.d("repository response1", "Fetching users:")
        return try {
            val response = apiService.getRandomUsers(number)
            //Log.d("repository response", "Fetched users: ${response.body()}")
            if (response.isSuccessful) {
                response.body()?.results
            } else {
               // Log.e("UserRepository", "Response not successful: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            //Log.e("UserRepository", "Error fetching users", e)
            null
        }
    }
}
