package com.example.habit_tracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.habit_tracker.screen.reminderApp
import com.example.habit_tracker.viewmodel.ReminderViewModel


class MainActivity : ComponentActivity() {
    private val vm: ReminderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            reminderApp(vm)
        }
    }
}



