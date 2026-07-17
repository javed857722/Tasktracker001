package com.example.tasktracker001.ui

import android.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Composable function that displays the Analytics Dashboard.
 *
 * This screen provides visual insights into task data using charts.
 * It features a Pie Chart showing the distribution of completed vs. pending tasks,
 * and a Bar Chart showing the number of tasks completed per day.
 *
 * @param navController The [NavController] used for navigation.
 * @param taskViewModel The [TaskViewModel] that provides the task data for analysis.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(navController: NavController, taskViewModel: TaskViewModel) {
    val tasks by taskViewModel.tasks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analytics Dashboard") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Pie chart for task status
            val statusCounts = tasks.groupingBy { it.isCompleted }.eachCount()
            val pieEntries = statusCounts.map { (isCompleted, count) ->
                PieEntry(count.toFloat(), if (isCompleted) "Completed" else "Pending")
            }
            val pieDataSet = PieDataSet(pieEntries, "Task Status").apply {
                colors = listOf(Color.GREEN, Color.RED)
            }
            val pieData = PieData(pieDataSet)

            AndroidView(factory = { context ->
                PieChart(context).apply {
                    data = pieData
                    description.isEnabled = false
                    invalidate()
                }
            }, modifier = Modifier.weight(1f))

            // Bar chart for completed tasks per day
            val completedTasksByDay = tasks
                .filter { it.isCompleted && it.dueDate != null }
                .groupingBy { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it.dueDate) }
                .eachCount()
            val barEntries = completedTasksByDay.entries.mapIndexed { index, entry ->
                BarEntry(index.toFloat(), entry.value.toFloat())
            }
            val barDataSet = BarDataSet(barEntries, "Completed Tasks")
            val barData = BarData(barDataSet)
            val xAxisLabels = completedTasksByDay.keys.toList()

            AndroidView(factory = { context ->
                BarChart(context).apply {
                    data = barData
                    description.isEnabled = false
                    xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
                    xAxis.setLabelCount(xAxisLabels.size, true)
                    invalidate()
                }
            }, modifier = Modifier.weight(1f))
        }
    }
}