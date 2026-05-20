package com.qiuye.calendarkotlin

import android.app.Application
import com.qiuye.calendarkotlin.tasks.TasksGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

import android.util.Log

class CalendarApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        TasksGraph.initialize(this)
        applicationScope.launch {
            runCatching {
                TasksGraph.reminderService().restoreSchedules()
            }.onFailure { e ->
                Log.e("CalendarApplication", "Failed to restore schedules on app launch", e)
            }
        }
    }
}


