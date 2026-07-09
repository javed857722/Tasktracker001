package com.example.tasktracker001.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository class that abstracts access to the Comment data source.
 *
 * @property commentDao The Data Access Object for comments.
 */
class CommentRepository(private val commentDao: CommentDao) {

    /**
     * Retrieves all comments for a specific task as a Flow.
     * @param taskId The ID of the task.
     * @return A Flow emitting the list of comments.
     */
    fun getCommentsForTask(taskId: Int): Flow<List<Comment>> = commentDao.getCommentsForTask(taskId)

    /**
     * Inserts a new comment into the data source.
     * @param comment The comment to insert.
     */
    suspend fun insert(comment: Comment) {
        commentDao.insert(comment)
    }
}
