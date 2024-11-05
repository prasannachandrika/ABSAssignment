package com.example.absassignment.data.repository

import android.util.Log
import com.example.absassignment.data.model.User
import com.example.absassignment.data.network.ApiService
import javax.inject.Inject

class UserRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getUsers(number: Int): List<User>? {
        Log.d("repository response1", "Fetched users:")
        return try {
            val response = apiService.getRandomUsers(number)
            Log.d("repository response", "Fetched users: ${response.body()}")
            if (response.isSuccessful) {
                response.body()?.results
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error fetching users", e)
            null
        }
    }
}
