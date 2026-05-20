package com.qiuye.calendarkotlin.tasks.data

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ReminderDatabaseMigrationTest {

    private val TEST_DB = "migration-test"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        ReminderDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        var db = helper.createDatabase(TEST_DB, 1)

        // db has schema version 1. insert some data using SQL queries.
        // You cannot use DAO classes because they expect the latest schema.
        db.execSQL(
            """
            INSERT INTO reminders (title, note, scheduledAtMillis, isCompleted, createdAtMillis, updatedAtMillis) 
            VALUES ('Test Title', 'Test Note', 1000, 0, 500, 500)
            """.trimIndent()
        )

        // Nullable test for v1 (as title and note were String? or implicit nulls allowed)
        db.execSQL(
            """
            INSERT INTO reminders (title, note, scheduledAtMillis, isCompleted, createdAtMillis, updatedAtMillis) 
            VALUES (null, null, 2000, 1, 600, 600)
            """.trimIndent()
        )

        // Prepare for the next version.
        db.close()

        // Re-open the database with version 2 and provide
        // MIGRATION_1_2 as the migration process.
        db = helper.runMigrationsAndValidate(TEST_DB, 2, true, ReminderDatabase.MIGRATION_1_2)

        // MigrationTestHelper automatically verifies the schema changes,
        // but you need to validate that the data was migrated properly.
        val cursor = db.query("SELECT * FROM reminders ORDER BY id ASC")
        
        assertEquals(2, cursor.count)
        
        cursor.moveToFirst()
        assertEquals("Test Title", cursor.getString(cursor.getColumnIndexOrThrow("title")))
        assertEquals("Test Note", cursor.getString(cursor.getColumnIndexOrThrow("note")))
        assertEquals(1000L, cursor.getLong(cursor.getColumnIndexOrThrow("scheduledAtMillis")))
        assertEquals(0, cursor.getInt(cursor.getColumnIndexOrThrow("isCompleted")))

        cursor.moveToNext()
        assertEquals("", cursor.getString(cursor.getColumnIndexOrThrow("title"))) // COALESCE(title, '')
        assertEquals("", cursor.getString(cursor.getColumnIndexOrThrow("note"))) // COALESCE(note, '')
        assertEquals(2000L, cursor.getLong(cursor.getColumnIndexOrThrow("scheduledAtMillis")))
        assertEquals(1, cursor.getInt(cursor.getColumnIndexOrThrow("isCompleted")))
        
        cursor.close()
    }
}
