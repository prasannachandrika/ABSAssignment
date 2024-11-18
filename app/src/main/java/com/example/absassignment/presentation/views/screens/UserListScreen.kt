package com.example.absassignment.presentation.views.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.absassignment.R
import com.example.absassignment.data.data.User
import com.example.absassignment.viewmodel.ResultState
import com.example.absassignment.viewmodel.UserViewModel
import com.google.gson.Gson

@Composable
fun UserListScreen(navController: NavController, viewModel: UserViewModel = hiltViewModel()) {
    val userState by viewModel.userState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // Dark background
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Users List",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White, // White text color
            modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
        )

        // Initialize fetch of users on the first composition
        LaunchedEffect(Unit) {
            // Only load if we are still in a loading state or if data hasn't been loaded yet
            if (userState is ResultState.Loading) {
                viewModel.loadMoreUsers()
            }
        }

        when (userState) {
            is ResultState.Loading -> {
                // Display loading indicator
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .size(50.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(60.dp))
                }
            }
            is ResultState.Success -> {
                // Display user list
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    content = {
                        items((userState as ResultState.Success).data) { user ->
                            UserCard(navController, user)
                        }
                    }
                )
            }
            is ResultState.Error -> {
                // Display error message
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (userState as ResultState.Error).message,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Unexpected state",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


@Composable
fun UserCard(navController: NavController, user: User) {

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                val userJson = Gson().toJson(user)
                val encodedJson = Uri.encode(userJson)
                navController.navigate("userDetail/$encodedJson")
            },
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 100.dp)
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp) // Outer box size, slightly larger to accommodate the border
                    .clip(CircleShape) // Apply circular clipping to the border
                    .border(2.dp, Color.Black, CircleShape) // Border thickness and color
            ) {
                AsyncImage(
                    model = user.picture.thumbnail,
                    contentDescription = "User Profile Picture",
                    modifier = Modifier
                        .size(64.dp) // Size of the image itself
                        .clip(CircleShape),
                    placeholder = painterResource(id = R.drawable.ic_launcher_background),
                    error = painterResource(id = R.drawable.ic_launcher_background),
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = user.name.first + " " + user.name.last,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Blue // White text color
                )
                Spacer(modifier = Modifier.height(5.dp))
                AddressText(user)
            }
        }
    }
}

@Composable
fun AddressText(user: User) {
    Column {
        Text(
            text = user.location.country,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black // White text color
        )
        Text(
            text = "${user.location.state}, ${user.location.city}",
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black // White text color
        )
    }
}