package com.example.absassignment.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.absassignment.data.model.User
import com.example.absassignment.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private val _userState = MutableStateFlow<ResultState<List<User>>>(ResultState.Loading)
    val userState: StateFlow<ResultState<List<User>>> get() = _userState

    private val _allUsers = mutableListOf<User>() // All fetched users
    private var recordsFetched = 0 // Track how many records have been fetched
    private val fetchIncrement = 20 // Number of users to fetch in each request

    init {
        fetchUsers(fetchIncrement) // Load initial users
    }

    // Fetch users with the specified number of records
    fun fetchUsers(numberOfRecords: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fetchedUsers = repository.getUsers(numberOfRecords) // Pass the number of records to the repository
                withContext(Dispatchers.Main) {
                    if (fetchedUsers != null) {
                        if (fetchedUsers.isNotEmpty()) {
                            _allUsers.addAll(fetchedUsers) // Add fetched users to the list
                            recordsFetched += fetchedUsers.size // Update count of fetched records
                            _userState.value = ResultState.Success(_allUsers.toList()) // Update the state
                        } else {
                            _userState.value = ResultState.Error("No more users found.")
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _userState.value = ResultState.Error("An error occurred: ${e.message}")
                    Log.e("UserViewModel", "Error fetching users", e)
                }
            }
        }
    }

    // Public method to load more users
    fun loadMoreUsers() {
        fetchUsers(fetchIncrement) // Fetch more users
    }
}

// Define ResultState sealed class as before
sealed class ResultState<out T> {
    object Loading : ResultState<Nothing>()
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val message: String) : ResultState<Nothing>()
}
