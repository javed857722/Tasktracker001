package com.example.tasktracker001.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository class that abstracts access to Project data.
 *
 * @property projectDao The Data Access Object for projects.
 */
class ProjectRepository(private val projectDao: ProjectDao) {

    /**
     * Returns a Flow of all projects.
     */
    fun getAllProjects(): Flow<List<Project>> = projectDao.getAllProjects()

    /**
     * Finds a project by ID.
     */
    suspend fun getProjectById(projectId: Int): Project? {
        return projectDao.getProjectById(projectId)
    }

    /**
     * Inserts a new project.
     */
    suspend fun insert(project: Project) {
        projectDao.insert(project)
    }

    /**
     * Deletes a project.
     */
    suspend fun delete(project: Project) {
        projectDao.delete(project)
    }

    /**
     * Updates project info.
     */
    suspend fun update(project: Project) {
        projectDao.update(project)
    }
}
