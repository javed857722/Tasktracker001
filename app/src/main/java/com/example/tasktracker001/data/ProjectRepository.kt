package com.example.tasktracker001.data

import kotlinx.coroutines.flow.Flow

class ProjectRepository(private val projectDao: ProjectDao) {

    fun getAllProjects(): Flow<List<Project>> = projectDao.getAllProjects()

    suspend fun getProjectById(projectId: Int): Project? {
        return projectDao.getProjectById(projectId)
    }

    suspend fun insert(project: Project) {
        projectDao.insert(project)
    }

    suspend fun delete(project: Project) {
        projectDao.delete(project)
    }

    suspend fun update(project: Project) {
        projectDao.update(project)
    }
}