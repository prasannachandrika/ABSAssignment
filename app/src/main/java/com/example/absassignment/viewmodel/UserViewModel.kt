package com.example.absassignment.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.absassignment.data.model.User
import com.example.absassignment.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchUsers(number: Int) {
        viewModelScope.launch {
            val fetchedUsers = repository.getUsers(number)
            Log.d("FetchUsers", "Fetched users: $fetchedUsers")
            if (fetchedUsers != null) {
                _users.value = fetchedUsers
            } else {
                _error.value = "Failed to fetch users. Please try again."
            }
        }
    }
}
