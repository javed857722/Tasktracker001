package com.example.tasktracker001.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tasktracker001.data.Role

@Composable
fun AdminLoginScreen(navController: NavController, userViewModel: UserViewModel) {
    var emailOrUsername by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loggedInUser by userViewModel.loggedInUser.collectAsState()
    val loginError by userViewModel.loginError.collectAsState()

    LaunchedEffect(loggedInUser) {
        if (loggedInUser?.role == Role.ADMIN) {
            navController.navigate("admin_panel") {
                popUpTo("admin_login") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Admin Login",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedTextField(
            value = emailOrUsername,
            onValueChange = { emailOrUsername = it },
            label = { Text("Email or Username") },
            modifier = Modifier.fillMaxWidth(0.8f),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(0.8f),
            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
            singleLine = true
        )
        
        if (loginError != null) {
            Text(
                text = loginError!!,
                color = androidx.compose.ui.graphics.Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { userViewModel.login(emailOrUsername, password) },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Login")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        androidx.compose.material3.TextButton(
            onClick = { navController.navigate("admin_signup") }
        ) {
            Text("Don't have an admin account? Sign Up")
        }
    }
}
