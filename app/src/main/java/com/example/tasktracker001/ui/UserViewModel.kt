package com.example.tasktracker001.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktracker001.data.Role
import com.example.tasktracker001.data.User
import com.example.tasktracker001.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mindrot.jbcrypt.BCrypt

/**
 * ViewModel for managing user-related data, authentication, and profiles.
 *
 * @property repository The repository for user data operations.
 */
class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private val _loggedInUser = MutableStateFlow<User?>(null)
    val loggedInUser: StateFlow<User?> = _loggedInUser

    val allUsers: StateFlow<List<User>> = repository.getAllUsers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    private val _signUpError = MutableStateFlow<String?>(null)
    val signUpError: StateFlow<String?> = _signUpError

    fun login(emailOrUsername: String, password: String) {
        viewModelScope.launch {
            _loginError.value = null
            val user = repository.getUserByEmail(emailOrUsername) ?: repository.getUserByUsername(emailOrUsername)
            if (user != null) {
                val isPasswordCorrect = withContext(Dispatchers.Default) {
                    BCrypt.checkpw(password, user.passwordHash)
                }
                if (isPasswordCorrect) {
                    _loggedInUser.value = user
                } else {
                    _loginError.value = "Incorrect password"
                }
            } else {
                _loginError.value = "User not found"
            }
        }
    }

    fun signUp(username: String, email: String, password: String, role: Role = Role.USER) {
        viewModelScope.launch {
            _signUpError.value = null
            // Check if user already exists
            if (repository.getUserByEmail(email) != null || repository.getUserByUsername(username) != null) {
                _signUpError.value = "User already exists"
                return@launch
            }

            val hashedPassword = withContext(Dispatchers.Default) {
                BCrypt.hashpw(password, BCrypt.gensalt())
            }
            val user = User(
                username = username, 
                email = email, 
                passwordHash = hashedPassword, 
                role = role,
                isAdmin = (role == Role.ADMIN)
            )
            repository.insert(user)
            _loggedInUser.value = user // Log in immediately after sign up
        }
    }

    fun clearErrors() {
        _loginError.value = null
        _signUpError.value = null
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