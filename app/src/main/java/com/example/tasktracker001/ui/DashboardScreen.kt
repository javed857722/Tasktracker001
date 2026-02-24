package com.example.tasktracker001.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tasktracker001.data.Priority
import com.example.tasktracker001.data.Role
import com.example.tasktracker001.data.Task
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, userViewModel: UserViewModel, taskViewModel: TaskViewModel) {
    val tasks by taskViewModel.tasks.collectAsState()
    val loggedInUser by userViewModel.loggedInUser.collectAsState()
    var statusFilter by remember { mutableStateOf<Boolean?>(null) }
    var priorityFilter by remember { mutableStateOf<Priority?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var sortOption by remember { mutableStateOf(SortOption.None) }

    val filteredTasks = tasks.filter { task ->
        (statusFilter == null || task.isCompleted == statusFilter) &&
        (priorityFilter == null || task.priority == priorityFilter) &&
        (searchQuery.isEmpty() || task.title.contains(searchQuery, ignoreCase = true))
    }.let { 
        when (sortOption) {
            SortOption.None -> it
            SortOption.DueDate -> it.sortedBy { task -> task.dueDate }
            SortOption.Priority -> it.sortedByDescending { task -> task.priority }
            SortOption.Status -> it.sortedBy { task -> task.isCompleted }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("create_task") }) {
                Icon(Icons.Default.Add, contentDescription = "Create Task")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = {
                    userViewModel.logout()
                    navController.navigate("login") { popUpTo("login") { inclusive = true } }
                }) {
                    Text("Logout")
                }
                Button(onClick = { navController.navigate("analytics") }) {
                    Text("Analytics")
                }
                Button(onClick = { navController.navigate("calendar") }) {
                    Text("Calendar")
                }
                if (loggedInUser?.role == Role.ADMIN) {
                    Button(onClick = { navController.navigate("admin_panel") }) {
                        Text("Admin Panel")
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                UpcomingDeadlines(tasks = tasks)
                CompletedTasksSummary(tasks = tasks)
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TaskFilter(
                    onFilterChanged = { status, priority, _ ->
                        statusFilter = status
                        priorityFilter = priority
                    }
                )
                SortComponent(onSortChanged = { sortOption = it })
            }

            val density = LocalDensity.current
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredTasks, key = { it.id }) { task ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { dismissValue ->
                            if (dismissValue == SwipeToDismissBoxValue.StartToEnd || dismissValue == SwipeToDismissBoxValue.EndToStart) {
                                loggedInUser?.let { user ->
                                    taskViewModel.deleteTask(task, user.id, user.username)
                                }
                                true
                            } else {
                                false
                            }
                        },
                        positionalThreshold = { with(density) { 150.dp.toPx() } }
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                             val color = when (dismissState.targetValue) {
                                SwipeToDismissBoxValue.EndToStart -> Color.Red
                                SwipeToDismissBoxValue.StartToEnd -> Color.Red
                                else -> Color.Transparent
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color)
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.White,
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                )
                            }
                        }
                    ) { 
                        TaskListItem(navController, userViewModel, taskViewModel, task)
                    }
                }
            }
        }
    }
}

@Composable
fun TaskListItem(
    navController: NavController,
    userViewModel: UserViewModel,
    taskViewModel: TaskViewModel,
    task: Task
) {
    val loggedInUser by userViewModel.loggedInUser.collectAsState()
    ListItem(
        headlineContent = {
            Text(
                text = task.title,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
            )
        },
        supportingContent = {
            Column {
                Text("Priority: ${task.priority}")
                task.dueDate?.let {
                    Text("Due: ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(java.util.Date(it))}")
                }
            }
        },
        leadingContent = {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = {
                    loggedInUser?.let { user ->
                        val updatedTask = task.copy(isCompleted = it)
                        taskViewModel.updateTask(updatedTask, user.id, user.username)
                    }
                }
            )
        },
        modifier = Modifier
            .clickable { navController.navigate("task_detail/${task.id}") }
            .background(MaterialTheme.colorScheme.surface)
    )
}