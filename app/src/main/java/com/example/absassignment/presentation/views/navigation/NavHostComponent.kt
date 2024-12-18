package com.example.absassignment.presentation.views.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.absassignment.presentation.views.screens.UserDetailScreen
import com.example.absassignment.presentation.views.screens.UserListScreen

@Composable
fun NavHostComponent() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "userList") {
        composable("userList") {
            UserListScreen(navController)
        }
        composable("userDetail/{userJson}") { backStackEntry ->
            //val userId = backStackEntry.arguments?.getString("userId")
            val userJson = backStackEntry.arguments?.getString("userJson")
            if (userJson != null) {
                UserDetailScreen(navController,userJson)
            }
        }
    }
}
