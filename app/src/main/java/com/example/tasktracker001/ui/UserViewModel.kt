package com.example.tasktracker001.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktracker001.data.Role
import com.example.tasktracker001.data.User
import com.example.tasktracker001.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loggedInUser = MutableStateFlow<User?>(null)
    val loggedInUser: StateFlow<User?> = _loggedInUser

    val allUsers: StateFlow<List<User>> = repository.getAllUsers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun signUp(username: String, email: String, password: String, role: Role) {
        viewModelScope.launch {
            val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
            val user = User(username = username, email = email, passwordHash = hashedPassword, role = role)
            repository.insert(user)
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val user = repository.getUserByUsername(username)
            if (user != null && BCrypt.checkpw(password, user.passwordHash)) {
                _loggedInUser.value = user
            } else {
                // Handle failed login
            }
        }
    }

    fun logout() {
        _loggedInUser.value = null
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            repository.update(user)
        }
    }

    fun updateUserProfile(userId: Int, username: String, email: String) {
        viewModelScope.launch {
            val user = repository.getUserById(userId)
            user?.let {
                val updatedUser = it.copy(username = username, email = email)
                repository.update(updatedUser)
                _loggedInUser.value = updatedUser
            }
        }
    }

    fun updatePassword(password: String) {
        viewModelScope.launch {
            _loggedInUser.value?.let {
                val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
                val updatedUser = it.copy(passwordHash = hashedPassword)
                repository.update(updatedUser)
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            repository.delete(user)
        }
    }
}