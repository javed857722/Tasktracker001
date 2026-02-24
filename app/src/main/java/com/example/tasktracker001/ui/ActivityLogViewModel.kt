package com.example.tasktracker001.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktracker001.data.ActivityLog
import com.example.tasktracker001.data.ActivityLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ActivityLogViewModel(private val repository: ActivityLogRepository) : ViewModel() {

    fun getActivityLogsForTask(taskId: Int): Flow<List<ActivityLog>> {
        return repository.getActivityLogsForTask(taskId)
    }

    fun addActivityLog(taskId: Int, userId: Int, username: String, action: String) {
        viewModelScope.launch {
            val activityLog = ActivityLog(
                taskId = taskId,
                userId = userId,
                username = username,
                action = action,
                timestamp = System.currentTimeMillis()
            )
            repository.insert(activityLog)
        }
    }
}