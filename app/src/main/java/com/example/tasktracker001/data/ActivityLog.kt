package com.example.tasktracker001.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing a log of activity performed within the system.
 *
 * @property id Unique identifier for the log entry.
 * @property taskId The ID of the task associated with this action.
 * @property userId The ID of the user who performed the action.
 * @property username The username of the user who performed the action.
 * @property action A description of the action performed (e.g., "Task Created").
 * @property timestamp The time the action occurred.
 */
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

