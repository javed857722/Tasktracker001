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
import java.util.concurrent.TimeUnit

@Composable
fun UpcomingDeadlines(tasks: List<Task>) {
    val upcomingTasks = tasks.filter { task ->
        task.dueDate?.let { dueDate ->
            val diff = dueDate - System.currentTimeMillis()
            diff > 0 && diff <= TimeUnit.DAYS.toMillis(7)
        } ?: false
    }

    if (upcomingTasks.isNotEmpty()) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("Upcoming Deadlines", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            upcomingTasks.forEach { task ->
                val dueDate = task.dueDate?.let { java.text.SimpleDateFormat("yyyy-MM-dd").format(java.util.Date(it)) } ?: ""
                Text("${task.title} - Due: $dueDate")
            }
        }
    }
}