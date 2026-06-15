package com.qiuye.calendarkotlin.data

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.test.core.app.ApplicationProvider
import com.qiuye.calendarkotlin.BaseUnitTest
import com.qiuye.calendarkotlin.model.ShiftColorOption
import com.qiuye.calendarkotlin.model.ShiftDefinition
import java.io.File
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.flow.first

class CalendarRepositoryMigrationTest : BaseUnitTest() {

    private lateinit var testDataStore: androidx.datastore.core.DataStore<Preferences>

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        testDataStore = PreferenceDataStoreFactory.create(
            produceFile = { File(context.filesDir, "test_migration_store.preferences_pb") }
        )
        runBlocking {
            testDataStore.edit { it.clear() }
        }
    }

    @Test
    fun `migrates completely flat old keys to a default profile and global notes`() = runBlocking {
        // Pre-populate with completely flat keys
        val patternKey = stringPreferencesKey("pattern")
        val notesKey = stringPreferencesKey("notes")
        val cycleStartDateKey = stringPreferencesKey("cycle_start_date")
        
        testDataStore.edit { prefs ->
            prefs.clear()
            val pattern = listOf(ShiftDefinition("1", "Legacy Shift", ShiftColorOption.BLUE))
            prefs[patternKey] = Json.encodeToString(pattern)
            prefs[notesKey] = Json.encodeToString(mapOf("2024-01-01" to "Old note"))
            prefs[cycleStartDateKey] = "2024-01-01"
        }

        val repository = CalendarRepository(dataStore = testDataStore)
        val data = repository.getCurrentData()

        assertEquals("default", data.activeProfileId)
        assertEquals(1, data.profiles.size)
        val profile = data.profiles[0]
        assertEquals("默认方案", profile.name)
        assertEquals("2024-01-01", profile.cycleStartDate)
        assertEquals(1, profile.pattern.size)
        assertEquals("Legacy Shift", profile.pattern[0].name)
        
        // Assert notes migrated to global
        assertEquals("Old note", data.notes["2024-01-01"])
    }

    @Test
    fun `migrates legacy profiles array to new format and merges notes`() = runBlocking {
        val profilesKey = stringPreferencesKey("profiles")
        
        // Legacy profile structure where notes are inside the profile
        val legacyProfileJson = """
            [
                {
                    "id": "profile_1",
                    "name": "Old Profile 1",
                    "notes": {"2024-02-01": "Note from profile 1", "2024-03-01": "Conflict note A"}
                },
                {
                    "id": "profile_2",
                    "name": "Old Profile 2",
                    "notes": {"2024-02-02": "Note from profile 2", "2024-03-01": "Conflict note B"}
                }
            ]
        """.trimIndent()
        
        testDataStore.edit { prefs ->
            prefs.clear()
            prefs[profilesKey] = legacyProfileJson
            // Assume there might be some global notes already from partial migration
            val globalNotesKey = stringPreferencesKey("notes")
            prefs[globalNotesKey] = Json.encodeToString(mapOf("2024-01-01" to "Existing global note"))
        }

        val repository = CalendarRepository(dataStore = testDataStore)
        val data = repository.getCurrentData()
        
        assertEquals(2, data.profiles.size)
        assertEquals("Old Profile 1", data.profiles[0].name)
        assertEquals("Old Profile 2", data.profiles[1].name)
        
        assertEquals("Existing global note", data.notes["2024-01-01"])
        assertEquals("Note from profile 1", data.notes["2024-02-01"])
        assertEquals("Note from profile 2", data.notes["2024-02-02"])
        
        // Assert conflict notes are merged (concatenated)
        val conflictNote = data.notes["2024-03-01"]
        assertTrue("Expected to contain Conflict note A", conflictNote!!.contains("Conflict note A"))
        assertTrue("Expected to contain Conflict note B", conflictNote.contains("Conflict note B"))
    }
}
