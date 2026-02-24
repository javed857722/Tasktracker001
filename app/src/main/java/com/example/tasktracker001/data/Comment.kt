package com.example.tasktracker001.data

import androidx.room.Entity
import androidx.room.PrimaryKey

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