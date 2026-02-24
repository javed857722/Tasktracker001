package com.example.tasktracker001.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Insert
    suspend fun insert(comment: Comment)

    @Query("SELECT * FROM comments WHERE taskId = :taskId ORDER BY timestamp DESC")
    fun getCommentsForTask(taskId: Int): Flow<List<Comment>>
}