package com.example.tasktracker001.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tasktracker001.data.Priority

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFilter(
    onFilterChanged: (status: Boolean?, priority: Priority?, dueDate: Long?) -> Unit
) {
    var status by remember { mutableStateOf<Boolean?>(null) }
    var priority by remember { mutableStateOf<Priority?>(null) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Status Filter
        var statusExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = statusExpanded,
            onExpandedChange = { statusExpanded = !statusExpanded },
        ) {
            TextField(
                value = status?.let { if (it) "Complete" else "Incomplete" } ?: "All Statuses",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = statusExpanded,
                onDismissRequest = { statusExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("All Statuses") },
                    onClick = {
                        status = null
                        statusExpanded = false
                        onFilterChanged(status, priority, null)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Complete") },
                    onClick = {
                        status = true
                        statusExpanded = false
                        onFilterChanged(status, priority, null)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Incomplete") },
                    onClick = {
                        status = false
                        statusExpanded = false
                        onFilterChanged(status, priority, null)
                    }
                )
            }
        }

        // Priority Filter
        var priorityExpanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = priorityExpanded,
            onExpandedChange = { priorityExpanded = !priorityExpanded },
        ) {
            TextField(
                value = priority?.name ?: "All Priorities",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = priorityExpanded) },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = priorityExpanded,
                onDismissRequest = { priorityExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("All Priorities") },
                    onClick = {
                        priority = null
                        priorityExpanded = false
                        onFilterChanged(status, priority, null)
                    }
                )
                Priority.values().forEach { priorityValue ->
                    DropdownMenuItem(
                        text = { Text(priorityValue.name) },
                        onClick = {
                            priority = priorityValue
                            priorityExpanded = false
                            onFilterChanged(status, priority, null)
                        }
                    )
                }
            }
        }
    }
}