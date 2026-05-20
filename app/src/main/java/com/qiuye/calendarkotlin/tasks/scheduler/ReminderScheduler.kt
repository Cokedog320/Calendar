package com.qiuye.calendarkotlin.tasks.scheduler

import com.qiuye.calendarkotlin.tasks.data.ReminderEntity

interface ReminderScheduler {
    fun schedule(reminder: ReminderEntity)
    fun cancel(reminderId: Long)
    fun canScheduleExactAlarms(): Boolean
    fun reschedule(reminder: ReminderEntity) {
        cancel(reminder.id)
        schedule(reminder)
    }
}


