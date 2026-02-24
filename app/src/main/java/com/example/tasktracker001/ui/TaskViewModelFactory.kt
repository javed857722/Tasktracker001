package com.example.tasktracker001.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tasktracker001.data.ActivityLogRepository
import com.example.tasktracker001.data.TaskRepository
import com.example.tasktracker001.data.UserRepository

class TaskViewModelFactory(
    private val application: Application,
    private val taskRepository: TaskRepository,
    private val activityLogRepository: ActivityLogRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(application, taskRepository, activityLogRepository, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}