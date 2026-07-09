package com.example.tasktracker001.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the ActivityLog entity.
 */
@Dao
interface ActivityLogDao {
    /**
     * Inserts a new activity log entry.
     */
    @Insert
    suspend fun insert(activityLog: ActivityLog)

    /**
     * Retrieves activity logs associated with a specific task, ordered by timestamp.
     * @param taskId The ID of the task to get logs for.
     * @return A Flow of activity logs.
     */
    @Query("SELECT * FROM activity_logs WHERE taskId = :taskId ORDER BY timestamp DESC")
    fun getActivityLogsForTask(taskId: Int): Flow<List<ActivityLog>>
}
