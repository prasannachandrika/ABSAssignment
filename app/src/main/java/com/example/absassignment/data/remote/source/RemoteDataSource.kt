package com.example.absassignment.data.remote.source

import com.example.absassignment.data.model.UserResponse
import com.example.absassignment.data.remote.api.ApiService
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun fetchRandomUsers(results: Int): Response<UserResponse> {
        return apiService.getRandomUsers(results)
    }
}