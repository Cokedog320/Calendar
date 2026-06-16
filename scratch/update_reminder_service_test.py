import re
import os

test_file = "C:/Users/qiuye/Desktop/new_calender/Calendar/app/src/test/java/com/qiuye/calendarkotlin/tasks/service/ReminderServiceTest.kt"
with open(test_file, "r", encoding="utf-8") as f:
    content = f.read()

# Replace title = "...", note = "..." with input = "..."
# We will use regex to find saveReminder calls in tests and merge them.
# The structure is usually:
#             title = "...",
#             note = "...",

content = re.sub(
    r'title\s*=\s*"([^"]*)",\s*note\s*=\s*"([^"]*)",',
    lambda m: f'input = "{m.group(1)}\\n{m.group(2)}",',
    content
)
# For blank title: title = "   ", note = "note",
content = re.sub(
    r'title\s*=\s*"(\s*)",\s*note\s*=\s*"([^"]*)",',
    lambda m: f'input = "{m.group(1)}\\n{m.group(2)}",',
    content
)

# And add the split tests to ReminderServiceTest.kt
split_tests = """
    @Test
    fun saveReminder_splitsSingleLineInputToTitleAndEmptyNote() = runBlocking {
        scheduler.canScheduleExactAlarmsResult = false
        val result = service.saveReminder(
            reminderId = null,
            input = "去超市买牛奶",
            scheduledAtMillis = System.currentTimeMillis() + 60_000,
            allowPast = true
        )
        assertTrue(result is SaveReminderResult.Success)
        val success = result as SaveReminderResult.Success
        assertEquals("去超市买牛奶", success.reminder.title)
        assertEquals("", success.reminder.note)
    }

    @Test
    fun saveReminder_splitsMultiLineInputToTitleAndNote() = runBlocking {
        scheduler.canScheduleExactAlarmsResult = false
        val result = service.saveReminder(
            reminderId = null,
            input = "去超市买牛奶\\n买脱脂的\\n顺便带包盐",
            scheduledAtMillis = System.currentTimeMillis() + 60_000,
            allowPast = true
        )
        assertTrue(result is SaveReminderResult.Success)
        val success = result as SaveReminderResult.Success
        assertEquals("去超市买牛奶", success.reminder.title)
        assertEquals("买脱脂的\\n顺便带包盐", success.reminder.note)
    }

    @Test
    fun saveReminder_handlesWhitespaceInInput() = runBlocking {
        scheduler.canScheduleExactAlarmsResult = false
        val result = service.saveReminder(
            reminderId = null,
            input = "   \\n  some note  ",
            scheduledAtMillis = System.currentTimeMillis() + 60_000,
            allowPast = true
        )
        assertTrue(result is SaveReminderResult.ValidationError)
    }
"""

content = content.replace("private class RecordingScheduler", split_tests + "\n    private class RecordingScheduler")

with open(test_file, "w", encoding="utf-8") as f:
    f.write(content)

# Delete ReminderTitleSplitTest.kt
os.remove("C:/Users/qiuye/Desktop/new_calender/Calendar/app/src/test/java/com/qiuye/calendarkotlin/tasks/data/ReminderTitleSplitTest.kt")
