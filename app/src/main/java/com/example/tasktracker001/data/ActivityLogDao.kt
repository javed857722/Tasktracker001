package com.example.tasktracker001.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityLogDao {
    @Insert
    suspend fun insert(activityLog: ActivityLog)

    @Query("SELECT * FROM activity_logs WHERE taskId = :taskId ORDER BY timestamp DESC")
    fun getActivityLogsForTask(taskId: Int): Flow<List<ActivityLog>>
}