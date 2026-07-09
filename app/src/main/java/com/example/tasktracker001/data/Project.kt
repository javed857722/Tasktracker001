package com.example.tasktracker001.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a project that groups multiple related tasks.
 *
 * @property id Unique identifier for the project.
 * @property name The name of the project.
 * @property description A brief summary of the project's goals.
 */
@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String
)
