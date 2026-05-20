package com.qiuye.calendarkotlin.tasks.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.qiuye.calendarkotlin.tasks.data.ReminderEntity
import com.qiuye.calendarkotlin.tasks.receiver.ReminderAlertReceiver

class AlarmReminderScheduler(
    private val context: Context
) : ReminderScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(reminder: ReminderEntity) {
        if (reminder.isCompleted || reminder.scheduledAtMillis <= System.currentTimeMillis()) {
            cancel(reminder.id)
            return
        }

        val pendingIntent = createPendingIntent(reminder.id)
        val triggerAtMillis = reminder.scheduledAtMillis

        if (!canScheduleExactAlarms()) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        }
    }

    override fun cancel(reminderId: Long) {
        alarmManager.cancel(createPendingIntent(reminderId))
    }

    override fun canScheduleExactAlarms(): Boolean = ExactAlarmPermission.canSchedule(context)

    private fun createPendingIntent(reminderId: Long): PendingIntent {
        val intent = Intent(context, ReminderAlertReceiver::class.java).apply {
            action = ACTION_REMINDER_ALERT
            putExtra(EXTRA_REMINDER_ID, reminderId)
        }
        return PendingIntent.getBroadcast(
            context,
            reminderId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        const val ACTION_REMINDER_ALERT = "com.qiuye.calendarkotlin.action.REMINDER_ALERT"
        const val EXTRA_REMINDER_ID = "extra_reminder_id"
    }
}


