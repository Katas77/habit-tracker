package com.example.habit_tracker.viewmodel

import androidx.lifecycle.viewModelScope
import com.example.habit_tracker.data.model.Reminder
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.habit_tracker.data.ReminderRepository
import com.example.habit_tracker.data.db.ReminderDatabase
import com.example.habit_tracker.service.WorkScheduler

class ReminderViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = ReminderRepository(ReminderDatabase.getInstance(application).reminderDao())

    val reminders = repo.getAll().stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    fun addReminder(title: String, intervalMinutes: Long) {
        if (title.isBlank()) return
        val reminder = Reminder(title = title.trim(), intervalMinutes = intervalMinutes, enabled = true)
        viewModelScope.launch {
            repo.insert(reminder)
            WorkScheduler.scheduleReminder(getApplication(), reminder.id, reminder.title, reminder.intervalMinutes)
        }
    }

    fun deleteReminder(reminder: Reminder) {
        viewModelScope.launch {
            repo.delete(reminder)
            WorkScheduler.cancelReminder(getApplication(), reminder.id)
        }
    }

    fun toggleEnabled(reminder: Reminder) {
        val new = reminder.copy(enabled = !reminder.enabled)
        viewModelScope.launch {
            repo.update(new)
            if (new.enabled) {
                WorkScheduler.scheduleReminder(getApplication(), new.id, new.title, new.intervalMinutes)
            } else {
                WorkScheduler.cancelReminder(getApplication(), new.id)
            }
        }
    }
}
