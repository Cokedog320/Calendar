package com.qiuye.calendarkotlin.tasks.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [ReminderEntity::class],
    version = 2,
    exportSchema = true
)
abstract class ReminderDatabase : RoomDatabase() {
    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var instance: ReminderDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE reminders_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        title TEXT NOT NULL,
                        note TEXT NOT NULL,
                        scheduledAtMillis INTEGER NOT NULL,
                        isCompleted INTEGER NOT NULL,
                        createdAtMillis INTEGER NOT NULL,
                        updatedAtMillis INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO reminders_new (
                        id,
                        title,
                        note,
                        scheduledAtMillis,
                        isCompleted,
                        createdAtMillis,
                        updatedAtMillis
                    )
                    SELECT
                        id,
                        COALESCE(title, ''),
                        COALESCE(note, ''),
                        scheduledAtMillis,
                        isCompleted,
                        createdAtMillis,
                        updatedAtMillis
                    FROM reminders
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE reminders")
                db.execSQL("ALTER TABLE reminders_new RENAME TO reminders")
            }
        }

        val ALL_MIGRATIONS = arrayOf(MIGRATION_1_2)

        fun getInstance(context: Context): ReminderDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ReminderDatabase::class.java,
                    "reminders.db"
                )
                .addMigrations(*ALL_MIGRATIONS)
                .build().also { instance = it }
            }
        }
    }
}


