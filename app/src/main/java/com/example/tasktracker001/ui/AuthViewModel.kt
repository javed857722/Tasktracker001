package com.example.tasktracker001.ui

import androidx.lifecycle.ViewModel
import com.example.tasktracker001.data.Role
import com.example.tasktracker001.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.mindrot.jbcrypt.BCrypt

class AuthViewModel : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun login(email: String, password: String) {
        // This is a mock implementation. In a real app, you would have a secure authentication system.
        val role = if (email.contains("admin", ignoreCase = true)) Role.ADMIN else Role.USER
        val passwordHash = BCrypt.hashpw(password, BCrypt.gensalt()) // In a real app, you'd check against a stored hash
        _user.value = User(username = email, email = email, passwordHash = passwordHash, role = role)
    }

    fun signUp(email: String, password: String) {
        // This is a mock implementation. In a real app, you would have a secure authentication system.
        val role = if (email.contains("admin", ignoreCase = true)) Role.ADMIN else Role.USER
        val passwordHash = BCrypt.hashpw(password, BCrypt.gensalt())
        _user.value = User(username = email, email = email, passwordHash = passwordHash, role = role)
    }

    fun logout() {
        _user.value = null
    }
}
