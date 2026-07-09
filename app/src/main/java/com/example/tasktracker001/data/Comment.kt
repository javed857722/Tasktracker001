package com.example.tasktracker001.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing a comment on a task.
 *
 * @property id Unique ID for the comment.
 * @property taskId The ID of the task this comment belongs to.
 * @property userId The ID of the user who posted the comment.
 * @property username The username of the user who posted the comment.
 * @property text The content of the comment.
 * @property timestamp The time the comment was created.
 */
@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val taskId: Int,
    val userId: Int,
    val username: String,
    val text: String,
    val timestamp: Long
)