package com.example.tasktracker001.ui

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tasktracker001.data.Priority
import com.example.tasktracker001.data.Role
import com.example.tasktracker001.data.Task
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(navController: NavController, taskViewModel: TaskViewModel, userViewModel: UserViewModel, projectViewModel: ProjectViewModel, taskId: Int) {
    val scope = rememberCoroutineScope()
    var task by remember { mutableStateOf<Task?>(null) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf<Long?>(null) }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    var assignedToUserId by remember { mutableStateOf<Int?>(null) }
    var projectId by remember { mutableStateOf<Int?>(null) }
    val users by userViewModel.allUsers.collectAsState()
    val loggedInUser by userViewModel.loggedInUser.collectAsState()
    val projects by projectViewModel.projects.collectAsState()

    LaunchedEffect(taskId) {
        scope.launch {
            task = taskViewModel.getTaskById(taskId)
            task?.let {
                title = it.title
                description = it.description
                dueDate = it.dueDate
                priority = it.priority
                assignedToUserId = it.assignedToUserId
                projectId = it.projectId
            }
        }
    }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            dueDate = selectedDate.timeInMillis
        },
        year, month, day
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Edit Task") })
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            
            var priorityExpanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = priorityExpanded,
                onExpandedChange = { priorityExpanded = !priorityExpanded },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                TextField(
                    value = priority.name,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = priorityExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = priorityExpanded,
                    onDismissRequest = { priorityExpanded = false }
                ) {
                    Priority.values().forEach { priorityValue ->
                        DropdownMenuItem(
                            text = { Text(priorityValue.name) },
                            onClick = {
                                priority = priorityValue
                                priorityExpanded = false
                            }
                        )
                    }
                }
            }

            if (loggedInUser?.role == Role.ADMIN || loggedInUser?.role == Role.MANAGER) {
                var userExpanded by remember { mutableStateOf(false) }
                val selectedUser = users.find { it.id == assignedToUserId }?.username ?: "Assign to user"

                ExposedDropdownMenuBox(
                    expanded = userExpanded,
                    onExpandedChange = { userExpanded = !userExpanded },
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                    TextField(
                        value = selectedUser,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = userExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = userExpanded,
                        onDismissRequest = { userExpanded = false }
                    ) {
                        users.forEach { user ->
                            DropdownMenuItem(
                                text = { Text(user.username) },
                                onClick = {
                                    assignedToUserId = user.id
                                    userExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            var projectExpanded by remember { mutableStateOf(false) }
            val selectedProject = projects.find { it.id == projectId }?.name ?: "Select Project"

            ExposedDropdownMenuBox(
                expanded = projectExpanded,
                onExpandedChange = { projectExpanded = !projectExpanded },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                TextField(
                    value = selectedProject,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = projectExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = projectExpanded,
                    onDismissRequest = { projectExpanded = false }
                ) {
                    projects.forEach { project ->
                        DropdownMenuItem(
                            text = { Text(project.name) },
                            onClick = {
                                projectId = project.id
                                projectExpanded = false
                            }
                        )
                    }
                }
            }

            Button(
                onClick = { datePickerDialog.show() },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text(text = dueDate?.let { "Due Date: ${java.text.SimpleDateFormat("yyyy-MM-dd").format(it)}" } ?: "Set Due Date")
            }
            Button(
                onClick = {
                    task?.let {
                        val updatedTask = it.copy(
                            title = title,
                            description = description,
                            dueDate = dueDate,
                            priority = priority,
                            assignedToUserId = assignedToUserId,
                            projectId = projectId
                        )
                        loggedInUser?.let {
                            taskViewModel.updateTask(updatedTask, it.id, it.username)
                        }
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("Update Task")
            }
        }
    }
}