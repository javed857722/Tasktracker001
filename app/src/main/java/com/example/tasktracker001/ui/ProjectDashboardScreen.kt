package com.example.tasktracker001.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
 * Composable function that displays the Project Dashboard screen.
 *
 * This screen allows users to view a list of all existing projects and navigate to their
 * individual details. It also provides an interface to create new projects by
 * specifying a name and description.
 *
 * @param navController The [NavController] used for navigating to project details.
 * @param projectViewModel The [ProjectViewModel] that manages project-related data and operations.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDashboardScreen(navController: NavController, projectViewModel: ProjectViewModel) {
    val projects by projectViewModel.projects.collectAsState()
    var newProjectName by remember { mutableStateOf("") }
    var newProjectDescription by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Projects", modifier = Modifier.padding(bottom = 16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(projects) {
                project ->
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    onClick = {
                        navController.navigate("project_detail/${project.id}")
                    }) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(project.name)
                        Text(project.description)
                    }
                }
            }
        }

        Column {
            OutlinedTextField(
                value = newProjectName,
                onValueChange = { newProjectName = it },
                label = { Text("New Project Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = newProjectDescription,
                onValueChange = { newProjectDescription = it },
                label = { Text("New Project Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    projectViewModel.addProject(newProjectName, newProjectDescription)
                    newProjectName = ""
                    newProjectDescription = ""
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Add Project")
            }
        }
    }
}