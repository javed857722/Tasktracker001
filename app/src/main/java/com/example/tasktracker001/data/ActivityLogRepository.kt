package com.example.tasktracker001.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository class that abstracts access to Activity Log data.
 *
 * @property activityLogDao The Data Access Object for activity logs.
 */
class ActivityLogRepository(private val activityLogDao: ActivityLogDao) {

    /**
     * Retrieves all activity logs for a specific task.
     */
    fun getActivityLogsForTask(taskId: Int): Flow<List<ActivityLog>> = activityLogDao.getActivityLogsForTask(taskId)

    /**
     * Inserts a new activity log entry.
     */
    suspend fun insert(activityLog: ActivityLog) {
        activityLogDao.insert(activityLog)
    }
}
