package com.example.tasktracker001.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the Task entity.
 * Provides methods for performing CRUD operations on tasks in the Room database.
 */
@Dao
interface TaskDao {
    /**
     * Inserts a new task into the database.
     * @param task The task to be inserted.
     * @return The row ID of the newly inserted task.
     */
    @Insert
    suspend fun insert(task: Task): Long

    /**
     * Retrieves all tasks from the database as a Flow.
     * @return A Flow emitting a list of all tasks.
     */
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<Task>>

    /**
     * Retrieves a specific task by its unique ID.
     * @param taskId The ID of the task to retrieve.
     * @return The task if found, null otherwise.
     */
    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Int): Task?

    /**
     * Deletes a task from the database.
     * @param task The task to be deleted.
     */
    @Delete
    suspend fun delete(task: Task)

    /**
     * Updates an existing task in the database.
     * @param task The task with updated information.
     */
    @Update
    suspend fun update(task: Task)
}
