package com.example.absassignment.views.screen

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.absassignment.R
import com.example.absassignment.data.model.User
import com.example.absassignment.viewmodel.UserViewModel
import com.google.gson.Gson

@Composable
fun UserListScreen(navController: NavController, viewModel: UserViewModel = hiltViewModel()) {
    val users by viewModel.users.observeAsState(emptyList())
    val error by viewModel.error.observeAsState("")
    val (userCount, setUserCount) = remember { mutableStateOf("") }
    Column {
        //Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = userCount,
            onValueChange = { setUserCount(it) },
            label = { Text("Enter number of users") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 20.dp, start = 25.dp, end = 25.dp)
        )
        Button(
            onClick = {
                val count = userCount.toIntOrNull() ?: 0 // Convert to Int, default to 0 if invalid
                if (count > 0) {
                    viewModel.fetchUsers(count) // Fetch the specified number of users
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Fetch Users")
        }

        LaunchedEffect(Unit) {
           viewModel.fetchUsers(50)
        }

        LazyColumn {
            items(users) { user ->
                UserCard(navController,user)


            }
        }
    }
}

@Composable
fun UserCard(navController: NavController,user: User) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                val userJson = Gson().toJson(user)
                val encodedJson = Uri.encode(userJson)
                navController.navigate("userDetail/$encodedJson")
            }
            .background(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)

    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .defaultMinSize(minHeight = 100.dp)
            .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {

            Box(
                modifier = Modifier
                    .size(64.dp) // Outer box size, slightly larger to accommodate the border
                    .clip(CircleShape) // Apply circular clipping to the border
                    .border(2.dp, Color.Green, CircleShape) // Border thickness and color
            ) {
                AsyncImage(
                    model = user.picture.thumbnail,
                    contentDescription = "User Profile Picture",
                    modifier = Modifier
                        .size(60.dp) // Size of the image itself
                        .clip(CircleShape),
                    placeholder = painterResource(id = R.drawable.ic_launcher_background),
                    error = painterResource(id = R.drawable.ic_launcher_background),
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
                verticalArrangement = Arrangement.Center,
            ){
                Text(text = user.name.first+" "+user.name.last,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    // modifier = Modifier.weight(1f), // Allows it to take available space
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Blue
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
            color = Color.Gray
        )
        Text(
            text = "${user.location.state}, ${user.location.city}",
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Gray
        )
    }
}
