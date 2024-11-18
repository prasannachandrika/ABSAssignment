package com.example.absassignment.domainn

import com.example.absassignment.data.model.User

interface UserRepository {
    suspend fun getUsers(number: Int): List<User>?
}