package com.example.tasktracker001.data

import kotlinx.coroutines.flow.Flow

class CommentRepository(private val commentDao: CommentDao) {

    fun getCommentsForTask(taskId: Int): Flow<List<Comment>> = commentDao.getCommentsForTask(taskId)

    suspend fun insert(comment: Comment) {
        commentDao.insert(comment)
    }
}