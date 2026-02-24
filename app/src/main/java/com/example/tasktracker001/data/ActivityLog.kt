package com.example.tasktracker001.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_logs")
data class ActivityLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val taskId: Int,
    val userId: Int,
    val username: String,
    val action: String,
    val timestamp: Long
)
