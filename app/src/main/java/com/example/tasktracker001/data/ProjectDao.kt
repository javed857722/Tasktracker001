package com.example.tasktracker001.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the Project entity.
 */
@Dao
interface ProjectDao {
    /**
     * Inserts a new project.
     */
    @Insert
    suspend fun insert(project: Project)

    /**
     * Retrieves all projects.
     */
    @Query("SELECT * FROM projects")
    fun getAllProjects(): Flow<List<Project>>

    /**
     * Finds a project by its ID.
     */
    @Query("SELECT * FROM projects WHERE id = :projectId")
    suspend fun getProjectById(projectId: Int): Project?

    /**
     * Deletes a project.
     */
    @Delete
    suspend fun delete(project: Project)

    /**
     * Updates an existing project.
     */
    @Update
    suspend fun update(project: Project)
}
