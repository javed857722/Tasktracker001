package com.example.tasktracker001.ui

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.tasktracker001.data.Priority
import com.example.tasktracker001.data.Role
import com.example.tasktracker001.util.NotificationReceiver
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(navController: NavController, taskViewModel: TaskViewModel, userViewModel: UserViewModel, projectViewModel: ProjectViewModel) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf<Long?>(null) }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    var assignedToUserId by remember { mutableStateOf<Int?>(null) }
    var projectId by remember { mutableStateOf<Int?>(null) }
    var attachments by remember { mutableStateOf<List<String>>(emptyList()) }
    val context = LocalContext.current
    val users by userViewModel.allUsers.collectAsState()
    val loggedInUser by userViewModel.loggedInUser.collectAsState()
    val projects by projectViewModel.projects.collectAsState()

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, pickedYear: Int, pickedMonth: Int, pickedDayOfMonth: Int ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(pickedYear, pickedMonth, pickedDayOfMonth)
            dueDate = selectedDate.timeInMillis
        },
        year, month, day
    )

    val attachmentLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
        attachments = uris.map { it.toString() }
    }

    Column(modifier = Modifier.padding(16.dp)) {
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
            onClick = { attachmentLauncher.launch("*/*") },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Add Attachments (${attachments.size})")
        }
        Button(
            onClick = {
                if (title.isBlank()) {
                    Toast.makeText(context, "Title cannot be empty", Toast.LENGTH_SHORT).show()
                } else {
                    val finalAssignedToUserId = if (loggedInUser?.role == Role.USER) loggedInUser?.id else assignedToUserId
                    loggedInUser?.let {
                        taskViewModel.addTask(title, description, dueDate, priority, finalAssignedToUserId, projectId, attachments, it.id, it.username)
                    }
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Add Task")
        }
        Button(
            onClick = {
                scheduleNotification(context, title)
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Schedule Notification")
        }
    }
}

fun scheduleNotification(context: Context, taskTitle: String) {
    val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (alarmManager?.canScheduleExactAlarms() == false) {
            Toast.makeText(context, "Please grant permission to schedule exact alarms", Toast.LENGTH_LONG).show()
            return
        }
    }

    val intent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra("task_title", taskTitle)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val timeInMillis = System.currentTimeMillis() + 10000 // 10 seconds

    alarmManager?.setExact(
        AlarmManager.RTC_WAKEUP,
        timeInMillis,
        pendingIntent
    )
}