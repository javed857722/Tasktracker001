package com.example.tasktracker001.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

/**
 * Composable function that represents the Admin Task Oversight screen.
 *
 * This screen provides administrators with a comprehensive view of all tasks
 * within the system. It allows them to monitor task progress, priority,
 * and completion status. Clicking on a task navigates to its detailed view.
 *
 * @param navController The [NavController] used for navigating to task details.
 * @param taskViewModel The [TaskViewModel] that provides the list of all tasks.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTaskOversightScreen(navController: NavController, taskViewModel: TaskViewModel) {
    val tasks by taskViewModel.tasks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Oversight") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            items(tasks) { task ->
                ListItem(
                    headlineContent = { Text(task.title) },
                    supportingContent = { 
                        Column {
                            Text("Priority: ${task.priority}")
                            Text("Status: ${if (task.isCompleted) "Completed" else "Pending"}")
                        }
                     },
                    modifier = Modifier.clickable { navController.navigate("task_detail/${task.id}") }
                )
            }
        }
    }
}