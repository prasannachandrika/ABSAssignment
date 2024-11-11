package com.example.absassignment.views.screen
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.absassignment.R
import com.example.absassignment.data.model.User
import com.google.gson.Gson
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun UserDetailScreen(navController: NavController, userInfo: String?) {

    Column {

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.Start) // Align the button to the start (left) of the screen
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24), // Replace with your back arrow icon
                contentDescription = "Back"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
        ) {

            val decodedJson =
                userInfo?.let { URLDecoder.decode(it, StandardCharsets.UTF_8.toString()) }
            val user = decodedJson?.let { Gson().fromJson(it, User::class.java) }
            Box(
                modifier = Modifier
                    .size(250.dp) // Outer box size, slightly larger to accommodate the border
                    .clip(CircleShape) // Apply circular clipping to the border
                    .border(3.dp, Color.Black, CircleShape)
                    .align(Alignment.CenterHorizontally)// Border thickness and color
            ) {
                AsyncImage(
                    model = user?.picture?.large,
                    contentDescription = "User Profile Picture",
                    modifier = Modifier.size(250.dp).clip(CircleShape).aspectRatio(1f),

                    placeholder = painterResource(id = R.drawable.ic_launcher_background),
                    error = painterResource(id = R.drawable.ic_launcher_background),
                )

            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Contact Information",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp),
                color = Color.Blue
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Email", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Phone", style = MaterialTheme.typography.titleMedium)
                    // Add other labels here as needed
                }

                Column(
                    modifier = Modifier.weight(2f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (user != null) {
                        Text(text = user.email, style = MaterialTheme.typography.bodyMedium)
                        Text(text = user.phone, style = MaterialTheme.typography.bodyMedium)
                    }
                    // Add other values here as needed
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Name", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Gender", style = MaterialTheme.typography.titleMedium)
                    Text(text = "Age", style = MaterialTheme.typography.titleMedium)
                    // Add other labels here as needed
                }

                Column(
                    modifier = Modifier.weight(2f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (user != null) {
                        Text(
                            text = "${user.name.first} ${user.name.last}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(text = user.gender, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "${user.dob.age} years old",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}