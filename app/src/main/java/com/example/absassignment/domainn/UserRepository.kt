package com.example.absassignment.domainn

import com.example.absassignment.data.data.User

interface UserRepository {
    suspend fun getUsers(number: Int): List<User>?
}