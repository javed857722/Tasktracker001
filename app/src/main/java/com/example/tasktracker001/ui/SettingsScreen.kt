package com.example.tasktracker001.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Composable function that displays the Settings screen.
 *
 * This screen allows users to configure application-wide preferences,
 * such as enabling or disabling notifications. It also provides a link
 * to the user's profile screen.
 *
 * @param navController The [NavController] used for navigation.
 * @param userViewModel The [UserViewModel] providing user context.
 */
@Composable
fun SettingsScreen(navController: NavController, userViewModel: UserViewModel) {
    val loggedInUser by userViewModel.loggedInUser.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Settings", modifier = Modifier.padding(bottom = 16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Enable Notifications")
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = false, onCheckedChange = {})
        }

        Spacer(modifier = Modifier.padding(16.dp))

        Button(onClick = { navController.navigate("profile") }) {
            Text("View Profile")
        }
    }
}
