package com.example.tasktracker001.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository class that abstracts access to the User data source.
 *
 * @property userDao The Data Access Object for users.
 */
class UserRepository(private val userDao: UserDao) {

    /**
     * Retrieves all users from the data source.
     */
    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()

    /**
     * Finds a user by their numeric ID.
     */
    suspend fun getUserById(userId: Int): User? {
        return userDao.getUserById(userId)
    }

    /**
     * Inserts a new user into the system.
     */
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    /**
     * Finds a user by their unique username.
     */
    suspend fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }

    /**
     * Finds a user by their email address.
     */
    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    /**
     * Updates an existing user's information.
     */
    suspend fun update(user: User) {
        userDao.update(user)
    }

    /**
     * Deletes a user from the system.
     */
    suspend fun delete(user: User) {
        userDao.delete(user)
    }
}
