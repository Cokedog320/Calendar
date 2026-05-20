package com.qiuye.calendarkotlin.tasks.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ReminderRepository(
    private val reminderDao: ReminderDao
) {
    fun observeAll(): Flow<List<ReminderEntity>> = reminderDao.observeAll()

    fun observeById(id: Long): Flow<ReminderEntity?> = reminderDao.observeById(id)

    suspend fun getById(id: Long): ReminderEntity? = withContext(Dispatchers.IO) {
        reminderDao.getById(id)
    }

    suspend fun getFutureActiveReminders(now: Long): List<ReminderEntity> = withContext(Dispatchers.IO) {
        reminderDao.getFutureActiveReminders(now)
    }

    suspend fun insert(reminder: ReminderEntity): Long = withContext(Dispatchers.IO) {
        reminderDao.insert(reminder)
    }

    suspend fun update(reminder: ReminderEntity) = withContext(Dispatchers.IO) {
        reminderDao.update(reminder)
    }

    suspend fun delete(reminder: ReminderEntity) = withContext(Dispatchers.IO) {
        reminderDao.delete(reminder)
    }
}


