package com.example.tasktracker001.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktracker001.data.Comment
import com.example.tasktracker001.data.CommentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CommentViewModel(private val repository: CommentRepository) : ViewModel() {

    fun getCommentsForTask(taskId: Int): Flow<List<Comment>> {
        return repository.getCommentsForTask(taskId)
    }

    fun addComment(taskId: Int, userId: Int, username: String, text: String) {
        viewModelScope.launch {
            val comment = Comment(
                taskId = taskId,
                userId = userId,
                username = username,
                text = text,
                timestamp = System.currentTimeMillis()
            )
            repository.insert(comment)
        }
    }
}