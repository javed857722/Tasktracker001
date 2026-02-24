package com.example.tasktracker001.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktracker001.data.ActivityLog
import com.example.tasktracker001.data.ActivityLogRepository
import com.example.tasktracker001.data.Priority
import com.example.tasktracker001.data.Task
import com.example.tasktracker001.data.TaskRepository
import com.example.tasktracker001.data.UserRepository
import com.example.tasktracker001.util.cancelTaskReminder
import com.example.tasktracker001.util.scheduleTaskReminder
import com.example.tasktracker001.util.showNotification
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(application: Application, private val taskRepository: TaskRepository, private val activityLogRepository: ActivityLogRepository, private val userRepository: UserRepository) : AndroidViewModel(application) {

    val tasks: StateFlow<List<Task>> = taskRepository.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    suspend fun getTaskById(taskId: Int): Task? {
        return taskRepository.getTaskById(taskId)
    }

    fun addTask(title: String, description: String, dueDate: Long?, priority: Priority, assignedToUserId: Int?, projectId: Int?, attachments: List<String>, userId: Int, username: String) {
        viewModelScope.launch {
            val task = Task(title = title, description = description, dueDate = dueDate, priority = priority, assignedToUserId = assignedToUserId, projectId = projectId, attachments = attachments)
            taskRepository.insert(task)
            task.dueDate?.let { 
                scheduleTaskReminder(getApplication(), task.id, task.title, it)
            }
            addActivityLog(task.id, userId, username, "created this task")
            assignedToUserId?.let {
                val user = userRepository.getUserById(it)
                user?.let {
                    showNotification(getApplication(), "You have been assigned a new task: ${task.title}", task.id)
                }
            }
        }
    }

    fun deleteTask(task: Task, userId: Int, username: String) {
        viewModelScope.launch {
            taskRepository.delete(task)
            cancelTaskReminder(getApplication(), task.id)
            addActivityLog(task.id, userId, username, "deleted this task")
        }
    }

    fun updateTask(task: Task, userId: Int, username: String) {
        viewModelScope.launch {
            taskRepository.update(task)
            if (task.isCompleted) {
                cancelTaskReminder(getApplication(), task.id)
                addActivityLog(task.id, userId, username, "completed this task")
                task.assignedToUserId?.let {
                    val user = userRepository.getUserById(it)
                    user?.let {
                        showNotification(getApplication(), "${task.title} has been completed", task.id)
                    }
                }
            } else {
                task.dueDate?.let { 
                    scheduleTaskReminder(getApplication(), task.id, task.title, it)
                }
                addActivityLog(task.id, userId, username, "updated this task")
            }
        }
    }

    private fun addActivityLog(taskId: Int, userId: Int, username: String, action: String) {
        viewModelScope.launch {
            val activityLog = ActivityLog(
                taskId = taskId,
                userId = userId,
                username = username,
                action = action,
                timestamp = System.currentTimeMillis()
            )
            activityLogRepository.insert(activityLog)
        }
    }
}