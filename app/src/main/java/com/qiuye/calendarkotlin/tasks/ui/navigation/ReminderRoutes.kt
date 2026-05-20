package com.qiuye.calendarkotlin.tasks.ui.navigation

object ReminderRoutes {
    const val CALENDAR = "calendar"
    const val LIST = "reminder_list"
    const val EDIT_NEW = "reminder_edit/new?date={date}"
    const val EDIT = "reminder_edit/{reminderId}"
    const val REMINDER_ID_ARG = "reminderId"
    const val DATE_ARG = "date"

    fun edit(reminderId: Long): String = "reminder_edit/$reminderId"
    fun newWithDate(dateMillis: Long): String = "reminder_edit/new?date=$dateMillis"
}

