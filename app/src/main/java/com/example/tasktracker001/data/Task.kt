package com.example.tasktracker001.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val dueDate: Long? = null,
    val priority: Priority = Priority.MEDIUM,
    val assignedToUserId: Int? = null,
    val projectId: Int? = null,
    val attachments: List<String> = emptyList(),
    val timeSpent: Long = 0L
)