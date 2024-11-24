package com.example.absassignment.domainn

import com.example.absassignment.data.models.User

interface UserRepository {
    suspend fun getUsers(number: Int): List<User>?
}