package com.example.tasktracker001.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tasktracker001.data.Task
import kotlinx.coroutines.delay

@Composable
fun TimeTracking(
    task: Task,
    taskViewModel: TaskViewModel,
    userViewModel: UserViewModel
) {
    var isTracking by remember { mutableStateOf(false) }
    var timeSpent by remember { mutableStateOf(task.timeSpent) }
    val loggedInUser by userViewModel.loggedInUser.collectAsState()

    LaunchedEffect(isTracking) {
        if (isTracking) {
            while (isTracking) {
                delay(1000)
                timeSpent += 1000
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Time Spent: ${formatTime(timeSpent)}")
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { isTracking = true }) {
                Text("Start")
            }
            Button(onClick = {
                isTracking = false
                loggedInUser?.let { user ->
                    val updatedTask = task.copy(timeSpent = timeSpent)
                    taskViewModel.updateTask(updatedTask, user.id, user.username)
                }
            }) {
                Text("Stop")
            }
        }
    }
}

private fun formatTime(millis: Long): String {
    val hours = millis / 1000 / 3600
    val minutes = (millis / 1000 % 3600) / 60
    val seconds = millis / 1000 % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}