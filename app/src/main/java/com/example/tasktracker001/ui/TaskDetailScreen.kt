package com.example.tasktracker001.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
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
import androidx.navigation.NavController
import com.example.tasktracker001.data.Priority
import com.example.tasktracker001.data.User
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    taskViewModel: TaskViewModel,
    taskId: Int
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    var assignedToUserId by remember { mutableStateOf<Int?>(null) }
    var dueDate by remember { mutableStateOf<Long?>(null) }
    val loggedInUser by userViewModel.loggedInUser.collectAsState()
    val users by userViewModel.allUsers.collectAsState()

    val task by remember(taskId) {
        taskViewModel.tasks.map { tasks -> tasks.find { it.id == taskId } }
    }.collectAsState(initial = null)

    LaunchedEffect(task) {
        task?.let {
            title = it.title
            description = it.description
            priority = it.priority
            assignedToUserId = it.assignedToUserId
            dueDate = it.dueDate
        }
    }

    var isEditing by remember { mutableStateOf(false) }
    val showDatePicker = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Task" else "Task Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (isEditing) {
                        IconButton(onClick = { isEditing = false }) {
                            Icon(Icons.Default.Edit, contentDescription = "Cancel")
                        }
                    } else {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = {
                            task?.let {
                                loggedInUser?.let { user ->
                                    taskViewModel.deleteTask(it, user.id, user.username)
                                    navController.popBackStack()
                                }
                            }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = !isEditing
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                readOnly = !isEditing
            )

            var priorityExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = priorityExpanded,
                onExpandedChange = { priorityExpanded = !priorityExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = priority.name,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = priorityExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                if (isEditing) {
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
            }

            var userExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = userExpanded,
                onExpandedChange = { userExpanded = !userExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                val assignedUser = users.find { it.id == assignedToUserId }?.username ?: "Unassigned"
                TextField(
                    value = assignedUser,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = userExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                if (isEditing) {
                    ExposedDropdownMenu(
                        expanded = userExpanded,
                        onDismissRequest = { userExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Unassigned") },
                            onClick = {
                                assignedToUserId = null
                                userExpanded = false
                            }
                        )
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
            
            Button(onClick = { showDatePicker.value = true }) {
                Text(dueDate?.let { java.text.SimpleDateFormat("yyyy-MM-dd").format(java.util.Date(it)) } ?: "Set Due Date")
            }

            if (showDatePicker.value) {
                val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dueDate)
                DatePickerDialog(
                    onDismissRequest = { showDatePicker.value = false },
                    confirmButton = {
                        TextButton(onClick = {
                            dueDate = datePickerState.selectedDateMillis
                            showDatePicker.value = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker.value = false }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            task?.let {
                TimeTracking(task = it, taskViewModel = taskViewModel, userViewModel = userViewModel)
            }

            if (isEditing) {
                Button(
                    onClick = {
                        task?.let {
                            val updatedTask = it.copy(
                                title = title,
                                description = description,
                                priority = priority,
                                assignedToUserId = assignedToUserId,
                                dueDate = dueDate
                            )
                            loggedInUser?.let { user ->
                                taskViewModel.updateTask(updatedTask, user.id, user.username)
                            }
                            isEditing = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Update Task")
                }
            }
        }
    }
}