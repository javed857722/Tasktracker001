package com.example.tasktracker001.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Composable function that displays the User Profile screen.
 *
 * This screen allows the logged-in user to view and update their profile information,
 * including their username and email. It also displays their assigned role.
 *
 * @param navController The [NavController] used for navigation.
 * @param userViewModel The [UserViewModel] that manages the user's profile data.
 */
@Composable
fun ProfileScreen(navController: NavController, userViewModel: UserViewModel) {
    val loggedInUser by userViewModel.loggedInUser.collectAsState()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    loggedInUser?.let {
        username = it.username
        email = it.email
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Profile", modifier = Modifier.padding(bottom = 16.dp))

        loggedInUser?.let {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text("Role: ${it.role}")
            Spacer(modifier = Modifier.padding(16.dp))
            Button(onClick = { userViewModel.updateUserProfile(it.id, username, email) }) {
                Text("Save")
            }
        }
    }
}
