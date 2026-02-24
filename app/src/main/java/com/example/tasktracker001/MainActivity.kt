package com.example.tasktracker001

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tasktracker001.ui.theme.Tasktracker001Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tasktracker001Theme {
                NavGraph()
            }
        }
    }
}