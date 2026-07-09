package com.example.tasktracker001.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository class that abstracts access to the Task data source.
 *
 * @property taskDao The Data Access Object for tasks.
 */
class TaskRepository(private val taskDao: TaskDao) {

    /**
     * Retrieves all tasks from the data source as a Flow.
     */
    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    /**
     * Finds a task by its unique ID.
     */
    suspend fun getTaskById(taskId: Int): Task? {
        return taskDao.getTaskById(taskId)
    }

    /**
     * Inserts a new task.
     */
    suspend fun insert(task: Task): Long {
        return taskDao.insert(task)
    }

    /**
     * Deletes an existing task.
     */
    suspend fun delete(task: Task) {
        taskDao.delete(task)
    }

    /**
     * Updates an existing task.
     */
    suspend fun update(task: Task) {
        taskDao.update(task)
    }
}
