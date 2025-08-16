package com.example.habit_tracker.servise


import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object WorkScheduler {
    fun scheduleReminder(context: Context, reminderId: String, title: String, intervalMinutes: Long) {
        val workManager = WorkManager.getInstance(context)

        val input = workDataOf(ReminderWorker.KEY_TITLE to title)

        val request = PeriodicWorkRequestBuilder<ReminderWorker>(intervalMinutes, TimeUnit.MINUTES)
            .setInputData(input)
            .build()

        // Используем уникальное имя работы = id напоминания, чтобы можно было отменять/обновлять
        workManager.enqueueUniquePeriodicWork(
            reminderId,
            ExistingPeriodicWorkPolicy.REPLACE,
            request
        )
    }

    fun cancelReminder(context: Context, reminderId: String) {
        WorkManager.getInstance(context).cancelUniqueWork(reminderId)
    }
}
