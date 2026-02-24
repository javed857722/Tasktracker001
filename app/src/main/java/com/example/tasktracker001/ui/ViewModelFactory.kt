package com.example.tasktracker001.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tasktracker001.data.ActivityLogRepository
import com.example.tasktracker001.data.CommentRepository
import com.example.tasktracker001.data.ProjectRepository
import com.example.tasktracker001.data.TaskRepository
import com.example.tasktracker001.data.UserRepository

class ViewModelFactory(
    private val application: Application,
    private val taskRepository: TaskRepository? = null,
    private val userRepository: UserRepository? = null,
    private val projectRepository: ProjectRepository? = null,
    private val commentRepository: CommentRepository? = null,
    private val activityLogRepository: ActivityLogRepository? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java) && taskRepository != null && activityLogRepository != null && userRepository != null) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(application, taskRepository, activityLogRepository, userRepository) as T
        } else if (modelClass.isAssignableFrom(UserViewModel::class.java) && userRepository != null) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(ProjectViewModel::class.java) && projectRepository != null) {
            @Suppress("UNCHECKED_CAST")
            return ProjectViewModel(projectRepository) as T
        } else if (modelClass.isAssignableFrom(CommentViewModel::class.java) && commentRepository != null) {
            @Suppress("UNCHECKED_CAST")
            return CommentViewModel(commentRepository) as T
        } else if (modelClass.isAssignableFrom(ActivityLogViewModel::class.java) && activityLogRepository != null) {
            @Suppress("UNCHECKED_CAST")
            return ActivityLogViewModel(activityLogRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class or repository not provided")
    }
}