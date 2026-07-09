package com.example.tasktracker001.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the User entity.
 */
@Dao
interface UserDao {
    /**
     * Inserts a new user into the database.
     */
    @Insert
    suspend fun insert(user: User)

    /**
     * Finds a user by their unique username.
     */
    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): User?

    /**
     * Finds a user by their email address.
     */
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    /**
     * Finds a user by their numeric ID.
     */
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?

    /**
     * Returns a Flow containing the list of all users.
     */
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>

    /**
     * Updates an existing user's information.
     */
    @Update
    suspend fun update(user: User)

    /**
     * Deletes a user from the database.
     */
    @Delete
    suspend fun delete(user: User)
}

