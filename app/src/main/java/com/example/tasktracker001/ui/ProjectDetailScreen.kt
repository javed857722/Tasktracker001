package com.example.tasktracker001.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Composable function that displays the details of a specific project.
 *
 * This screen allows users to edit the project's name and description,
 * delete the project entirely, and view a list of all tasks associated
 * with this project. Clicking on a task navigates to its detail view.
 *
 * @param navController The [NavController] used for screen navigation.
 * @param projectViewModel The [ProjectViewModel] that handles project updates and deletion.
 * @param taskViewModel The [TaskViewModel] used to filter tasks belonging to this project.
 * @param projectId The unique identifier of the project to be displayed.
 */
@Composable
fun ProjectDetailScreen(
    navController: NavController,
    projectViewModel: ProjectViewModel,
    taskViewModel: TaskViewModel,
    projectId: Int
) {
    val projects by projectViewModel.projects.collectAsState()
    val project = projects.find { it.id == projectId }
    val tasks by taskViewModel.tasks.collectAsState()
    val projectTasks = tasks.filter { it.projectId == projectId }

    if (project != null) {
        var name by remember { mutableStateOf(project.name) }
        var description by remember { mutableStateOf(project.description) }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Edit Project", modifier = Modifier.padding(bottom = 16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Project Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Project Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    projectViewModel.updateProject(project.copy(name = name, description = description))
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Save Changes")
            }

            Button(
                onClick = {
                    projectViewModel.deleteProject(project)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Delete Project")
            }

            Text("Tasks in this project", modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))

            LazyColumn {
                items(projectTasks) { task ->
                    Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        onClick = { navController.navigate("task_detail/${task.id}") }) {
                        Text(text = task.title, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}