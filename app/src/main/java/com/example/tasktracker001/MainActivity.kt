package com.example.tasktracker001

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tasktracker001.ui.theme.JavedIqbalTaskTrackerTheme

/**
 * The main activity of the Task Tracker application.
 * This activity serves as the entry point and sets up the Compose UI with the application theme.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JavedIqbalTaskTrackerTheme {
                NavGraph()
            }
        }
    }
}
