package com.example.absassignment.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.absassignment.data.model.User
import com.example.absassignment.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private val _userState = MutableLiveData<ResultState<List<User>>>()
    val userState: LiveData<ResultState<List<User>>> get() = _userState

    fun fetchUsers(number: Int) {
        _userState.value = ResultState.Loading // Set state to loading
        viewModelScope.launch {
            try {
                val fetchedUsers = repository.getUsers(number)
                if (fetchedUsers != null) {
                    _userState.value = ResultState.Success(fetchedUsers) // Set state to success
                } else {
                    _userState.value = ResultState.Error("Failed to fetch users. Please try again.") // Set state to error
                }
            } catch (e: Exception) {
                _userState.value = ResultState.Error("An error occurred: ${e.message}")
            }
        }
    }

}
sealed class ResultState<out T> {
    object Loading : ResultState<Nothing>()
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val message: String) : ResultState<Nothing>()
}