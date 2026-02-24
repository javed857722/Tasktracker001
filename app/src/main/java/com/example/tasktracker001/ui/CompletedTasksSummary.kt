package com.example.tasktracker001.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasktracker001.data.Task

@Composable
fun CompletedTasksSummary(tasks: List<Task>) {
    val completedTasks = tasks.filter { it.isCompleted }

    if (completedTasks.isNotEmpty()) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("Completed Tasks Summary", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Total completed: ${completedTasks.size}")
        }
    }
}