import re

test_file = "C:/Users/qiuye/Desktop/new_calender/Calendar/app/src/test/java/com/qiuye/calendarkotlin/tasks/service/ReminderServiceTest.kt"
with open(test_file, "r", encoding="utf-8") as f:
    content = f.read()

# Fix ReminderEntity(..., input = "title\\nnote", ...) back to title and note
def fix_entity(match):
    input_val = match.group(1)
    parts = input_val.split('\\n')
    title = parts[0]
    note = parts[1] if len(parts) > 1 else ""
    return f'title = "{title}",\n                note = "{note}",'

content = re.sub(r'input\s*=\s*"([^"]*)",', fix_entity, content)

# Wait, but that will also revert saveReminder(input = "...")!
# We ONLY want to fix it inside ReminderEntity.
# Let's read from file again without regex globally.
