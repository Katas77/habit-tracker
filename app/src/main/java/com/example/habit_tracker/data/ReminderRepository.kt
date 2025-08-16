package com.example.habit_tracker.data



import com.example.habit_tracker.data.dao.ReminderDao
import com.example.habit_tracker.data.model.Reminder
import kotlinx.coroutines.flow.Flow

class ReminderRepository(private val dao: ReminderDao) {
    fun getAll(): Flow<List<Reminder>> = dao.getAll()
    suspend fun insert(reminder: Reminder) = dao.insert(reminder)
    suspend fun delete(reminder: Reminder) = dao.delete(reminder)
    suspend fun update(reminder: Reminder) = dao.update(reminder)
}
