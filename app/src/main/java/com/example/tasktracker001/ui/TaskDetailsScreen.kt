package com.example.tasktracker001.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tasktracker001.data.ActivityLog
import com.example.tasktracker001.data.Comment
import com.example.tasktracker001.data.Task
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(
    navController: NavController,
    taskViewModel: TaskViewModel,
    commentViewModel: CommentViewModel,
    activityLogViewModel: ActivityLogViewModel,
    userViewModel: UserViewModel,
    taskId: Int
) {
    val scope = rememberCoroutineScope()
    var task by remember { mutableStateOf<Task?>(null) }
    val comments by commentViewModel.getCommentsForTask(taskId).collectAsState(emptyList())
    val activityLogs by activityLogViewModel.getActivityLogsForTask(taskId).collectAsState(emptyList())
    val loggedInUser by userViewModel.loggedInUser.collectAsState()
    var newComment by remember { mutableStateOf("") }
    var timerJob by remember { mutableStateOf<Job?>(null) }
    var timeSpent by remember { mutableStateOf(0L) }

    LaunchedEffect(taskId) {
        scope.launch {
            task = taskViewModel.getTaskById(taskId)
            timeSpent = task?.timeSpent ?: 0L
        }
    }

    fun startTimer() {
        if (timerJob == null) {
            timerJob = scope.launch {
                while (true) {
                    delay(1000)
                    timeSpent++
                }
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        task?.let {
            val updatedTask = it.copy(timeSpent = timeSpent)
            loggedInUser?.let { user ->
                taskViewModel.updateTask(updatedTask, user.id, user.username)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Task Details") })
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            item {
                task?.let { task ->
                    Text("Title: ${task.title}", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    Text("Description: ${task.description}", fontSize = 16.sp)
                    Text("Priority: ${task.priority.name}", fontSize = 16.sp)
                    task.dueDate?.let {
                        Text("Due: ${SimpleDateFormat("yyyy-MM-dd", Locale.US).format(it)}", fontSize = 16.sp)
                    }
                    Text("Time Spent: ${timeSpent}s", fontSize = 16.sp)
                    Row {
                        Button(onClick = { startTimer() }) {
                            Text("Start")
                        }
                        Button(onClick = { stopTimer() }) {
                            Text("Stop")
                        }
                    }
                }
            }
            item {
                Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                    OutlinedTextField(
                        value = newComment,
                        onValueChange = { newComment = it },
                        label = { Text("Add a comment") },
                        modifier = Modifier.weight(1f)
                    )
                    Button(
                        onClick = {
                            loggedInUser?.let {
                                commentViewModel.addComment(taskId, it.id, it.username, newComment)
                                newComment = ""
                            }
                        },
                        modifier = Modifier.padding(start = 8.dp).align(Alignment.CenterVertically)
                    ) {
                        Text("Add")
                    }
                }
            }
            item {
                Text("Comments", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(top = 16.dp))
            }
            items(comments) { comment ->
                CommentItem(comment)
            }
            item {
                Text("Activity Log", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(top = 16.dp))
            }
            items(activityLogs) { activityLog ->
                ActivityLogItem(activityLog)
            }
        }
    }
}

@Composable
fun CommentItem(comment: Comment) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("${comment.username} at ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(comment.timestamp)}", fontSize = 12.sp)
            Text(comment.text, fontSize = 16.sp)
        }
    }
}

@Composable
fun ActivityLogItem(activityLog: ActivityLog) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("${activityLog.username} ${activityLog.action} at ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(activityLog.timestamp)}", fontSize = 12.sp)
        }
    }
}