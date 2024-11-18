package com.example.absassignment.domainn

import com.example.absassignment.data.data.User
import com.example.absassignment.repository.UserRepositoryImpl

import com.example.absassignment.viewmodel.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FetchUsersUseCase @Inject constructor(private val repository: UserRepositoryImpl) {

    operator fun invoke(numberOfRecords: Int): Flow<ResultState<List<User>>> = flow {
        emit(ResultState.Loading) // Emit loading state
        try {
            val fetchedUsers = repository.getUsers(numberOfRecords) // Fetch users from the repository

            // Emit success state if users are found, otherwise emit error
            if (fetchedUsers != null && fetchedUsers.isNotEmpty()) {
                emit(ResultState.Success(fetchedUsers)) // Emit success state with users
            } else {
                emit(ResultState.Error("An error occurred: No users found.")) // Emit error state
            }
        } catch (e: Exception) {
            emit(ResultState.Error("An error occurred: ${e.message}")) // Emit error state with exception message
        }
    }
}
