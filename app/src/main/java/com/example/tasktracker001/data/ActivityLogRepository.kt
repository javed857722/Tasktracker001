package com.example.tasktracker001.data

import kotlinx.coroutines.flow.Flow

class ActivityLogRepository(private val activityLogDao: ActivityLogDao) {

    fun getActivityLogsForTask(taskId: Int): Flow<List<ActivityLog>> = activityLogDao.getActivityLogsForTask(taskId)

    suspend fun insert(activityLog: ActivityLog) {
        activityLogDao.insert(activityLog)
    }
}