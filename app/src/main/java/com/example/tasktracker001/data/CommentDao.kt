package com.example.tasktracker001.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the Comment entity.
 */
@Dao
interface CommentDao {
    /**
     * Inserts a new comment into the database.
     */
    @Insert
    suspend fun insert(comment: Comment)

    /**
     * Retrieves all comments for a specific task, ordered by timestamp.
     * @param taskId The ID of the task.
     * @return A Flow emitting the list of comments.
     */
    @Query("SELECT * FROM comments WHERE taskId = :taskId ORDER BY timestamp DESC")
    fun getCommentsForTask(taskId: Int): Flow<List<Comment>>
}
