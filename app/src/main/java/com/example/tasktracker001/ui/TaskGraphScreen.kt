package com.example.tasktracker001.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

@Composable
fun TaskGraphScreen(taskViewModel: TaskViewModel) {
    val tasks by taskViewModel.tasks.collectAsState()

    val completedCount = tasks.count { it.isCompleted }.toFloat()
    val incompleteCount = tasks.count { !it.isCompleted }.toFloat()

    val entries = listOf(
        PieEntry(completedCount, "Completed"),
        PieEntry(incompleteCount, "Incomplete")
    )

    val dataSet = PieDataSet(entries, "Task Status").apply {
        colors = ColorTemplate.MATERIAL_COLORS.toList()
        valueTextColor = android.graphics.Color.BLACK
        valueTextSize = 16f
    }

    val pieData = PieData(dataSet)

    AndroidView(factory = { context ->
        PieChart(context).apply {
            data = pieData
            description.isEnabled = false
            invalidate()
        }
    })
}