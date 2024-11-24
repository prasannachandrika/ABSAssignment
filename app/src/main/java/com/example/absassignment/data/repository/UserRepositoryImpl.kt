package com.example.absassignment.repository

import com.example.absassignment.data.models.User
import com.example.absassignment.data.remote.ApiService
import com.example.absassignment.domainn.UserRepository
import javax.inject.Inject

// Data Layer - UserRepository Implementation
class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : UserRepository {

    override suspend fun getUsers(number: Int): List<User>? {

        return try {
            val response = apiService.getRandomUsers(number)

            if (response.isSuccessful) {
                response.body()?.results
            } else {

                null
            }
        } catch (e: Exception) {

            null
        }
    }
}
