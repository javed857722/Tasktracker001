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

/**
 * Composable function that represents the Admin Sign Up screen.
 *
 * This screen allows users to create a new account with the [Role.ADMIN] role.
 * It includes fields for username, email, and password, and performs validation
 * before allowing the sign-up process to proceed.
 *
 * @param navController The [NavController] used for navigating between screens.
 * @param userViewModel The [UserViewModel] that manages administrative user registration.
 */
@Composable
fun AdminSignUpScreen(navController: NavController, userViewModel: UserViewModel) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loggedInUser by userViewModel.loggedInUser.collectAsState()
    val signUpError by userViewModel.signUpError.collectAsState()

    // Validation logic
    val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val canSignUp = username.isNotBlank() && isEmailValid && password.length >= 6

    LaunchedEffect(loggedInUser) {
        if (loggedInUser?.role == Role.ADMIN) {
            navController.navigate("admin_panel") {
                popUpTo("admin_signup") { inclusive = true }
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
            text = "Admin Sign Up",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(0.8f),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            isError = email.isNotEmpty() && !isEmailValid,
            supportingText = {
                if (email.isNotEmpty() && !isEmailValid) {
                    Text("Enter a valid email address")
                }
            },
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
        
        if (signUpError != null) {
            Text(
                text = signUpError!!,
                color = androidx.compose.ui.graphics.Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { userViewModel.signUp(username, email, password, Role.ADMIN) },
            enabled = canSignUp,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Sign Up")
        }
    }
}
