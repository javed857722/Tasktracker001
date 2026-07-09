package com.example.tasktracker001.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a user within the Task Tracker system.
 *
 * @property id Unique identifier for the user.
 * @property username The user's chosen display name.
 * @property email The user's email address for authentication and notifications.
 * @property passwordHash The hashed version of the user's password.
 * @property role The role assigned to the user, determining their permissions.
 * @property isAdmin Boolean flag indicating if the user has administrative privileges.
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val email: String,
    val passwordHash: String,
    val role: Role = Role.USER,
    val isAdmin: Boolean = false
)