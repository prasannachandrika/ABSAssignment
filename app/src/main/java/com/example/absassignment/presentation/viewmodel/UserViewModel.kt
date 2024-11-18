package com.example.absassignment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.absassignment.data.model.User
import com.example.absassignment.domainn.FetchUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val fetchUsersUseCase: FetchUsersUseCase) : ViewModel() {

    private val _userState = MutableStateFlow<ResultState<List<User>>>(ResultState.Loading)
    val userState: StateFlow<ResultState<List<User>>> get() = _userState

    private val _allUsers = mutableListOf<User>() // All fetched users
    private var recordsFetched = 0 // Track how many records have been fetched
    private val fetchIncrement = 20 // Number of users to fetch in each request
    private var isDataLoaded = false  // Flag to check if data has been loaded

    init {
        loadMoreUsers() // Load initial users
    }

    // Fetch users with the specified number of records
    fun loadMoreUsers() {
        // Prevent fetching data again if already loaded
        if (isDataLoaded) {
            _userState.value = ResultState.Success(_allUsers) // Return the already loaded users
            return
        }

        viewModelScope.launch {
            // Update the state to loading before fetching data
            _userState.value = ResultState.Loading

            fetchUsersUseCase(fetchIncrement).collect { result ->
                _userState.value = result // Collect and update the state
                if (result is ResultState.Success) {
                    _allUsers.addAll(result.data) // Add fetched users to the list
                    recordsFetched += result.data.size // Update count of fetched records
                    isDataLoaded = true // Set flag to true after loading
                }
            }
        }
    }
}

// Define ResultState sealed class as before
sealed class ResultState<out T> {
    object Loading : ResultState<Nothing>()
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val message: String) : ResultState<Nothing>()
}
