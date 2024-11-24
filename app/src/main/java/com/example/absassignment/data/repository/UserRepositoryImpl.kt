package com.example.absassignment.repository

import com.example.absassignment.data.models.User
import com.example.absassignment.data.remote.source.RemoteDataSource
import com.example.absassignment.domainn.UserRepository
import javax.inject.Inject

// Data Layer - UserRepository Implementation

class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : UserRepository {
    override suspend fun getUsers(number: Int): List<User>? {
        return try {
            val response = remoteDataSource.fetchRandomUsers(number)
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

