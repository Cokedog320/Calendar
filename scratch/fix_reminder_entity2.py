import re

test_file = "C:/Users/qiuye/Desktop/new_calender/Calendar/app/src/test/java/com/qiuye/calendarkotlin/tasks/service/ReminderServiceTest.kt"
with open(test_file, "r", encoding="utf-8") as f:
    content = f.read()

# Fix ReminderEntity
def fix_entity_input(match):
    prefix = match.group(1)
    input_val = match.group(2)
    parts = input_val.split('\\n')
    title = parts[0]
    note = parts[1] if len(parts) > 1 else ""
    return prefix + f'title = "{title}",\n                note = "{note}",'

# Match `ReminderEntity(` followed by spaces/newlines then `id = 0,` then spaces/newlines `input = "..."`
content = re.sub(r'(ReminderEntity\(\s*id = \d+,\s*)input\s*=\s*"([^"]*)",', fix_entity_input, content)
content = re.sub(r'(ReminderEntity\(\s*id = existingId,\s*)input\s*=\s*"([^"]*)",', fix_entity_input, content)

with open(test_file, "w", encoding="utf-8") as f:
    f.write(content)

# Also fix ReminderStateTest.kt
state_test_file = "C:/Users/qiuye/Desktop/new_calender/Calendar/app/src/test/java/com/qiuye/calendarkotlin/tasks/ReminderStateTest.kt"
with open(state_test_file, "r", encoding="utf-8") as f:
    state_content = f.read()

# In ReminderStateTest.kt, the call might look like:
# viewModel.saveReminder(null, "Test Reminder", "note", futureMillis, 1, true)
# It takes (Long?, String, String, Long, Int, Boolean)
# We need to change to (Long?, String, Long, Int, Boolean)

state_content = re.sub(r'viewModel\.saveReminder\(null, "([^"]*)", "([^"]*)", ([^,]+), ([^,]+), (true|false)\)',
                       r'viewModel.saveReminder(null, "\1\\n\2", \3, \4, \5)', state_content)

with open(state_test_file, "w", encoding="utf-8") as f:
    f.write(state_content)

