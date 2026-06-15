import os

test_file = "C:/Users/qiuye/Desktop/new_calender/Calendar/app/src/test/java/com/qiuye/calendarkotlin/viewmodel/CalendarViewModelTest.kt"
with open(test_file, "r", encoding="utf-8") as f:
    content = f.read()

# Add the testClock definition if it's not there
if "val testClock" not in content:
    content = content.replace("class CalendarViewModelTest {",
                              "class CalendarViewModelTest {\n    private val testClock = java.time.Clock.fixed(java.time.Instant.parse(\"2026-06-15T12:00:00Z\"), java.time.ZoneId.of(\"UTC\"))")

# Update CalendarViewModel instantiations
# viewModel = CalendarViewModel(repository) -> viewModel = CalendarViewModel(repository, clock = testClock)
content = content.replace("val viewModel = CalendarViewModel(repository)", "val viewModel = CalendarViewModel(repository, clock = testClock)")

with open(test_file, "w", encoding="utf-8") as f:
    f.write(content)
