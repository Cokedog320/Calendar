import re

file_path = "C:/Users/qiuye/Desktop/new_calender/Calendar/app/src/main/java/com/qiuye/calendarkotlin/viewmodel/CalendarViewModel.kt"

with open(file_path, "r", encoding="utf-8") as f:
    content = f.read()

# Fix SheetState closeAll to match original closeAllSheets behavior
content = content.replace("isDaySheetVisible = false,\n", "")

# Fix references to XXXVisible.value = ...
content = re.sub(r'settingsVisible\.value\s*=\s*(true|false)', r'sheetState.value = sheetState.value.copy(isSettingsVisible = \1)', content)
content = re.sub(r'notesVisible\.value\s*=\s*(true|false)', r'sheetState.value = sheetState.value.copy(isNotesVisible = \1)', content)
content = re.sub(r'daySheetVisible\.value\s*=\s*(true|false)', r'sheetState.value = sheetState.value.copy(isDaySheetVisible = \1)', content)
content = re.sub(r'remindersVisible\.value\s*=\s*(true|false)', r'sheetState.value = sheetState.value.copy(isRemindersVisible = \1)', content)
content = re.sub(r'diaryListVisible\.value\s*=\s*(true|false)', r'sheetState.value = sheetState.value.copy(isDiaryListVisible = \1)', content)
content = re.sub(r'profileSelectVisible\.value\s*=\s*(true|false)', r'sheetState.value = sheetState.value.copy(isProfileSelectVisible = \1)', content)

# Replace the body of closeAllSheets entirely to use closeAll()
close_all_sheets_old = """    private fun closeAllSheets() {
        sheetState.value = sheetState.value.copy(isSettingsVisible = false)
        sheetState.value = sheetState.value.copy(isNotesVisible = false)
        sheetState.value = sheetState.value.copy(isRemindersVisible = false)
        sheetState.value = sheetState.value.copy(isDiaryListVisible = false)
        sheetState.value = sheetState.value.copy(isProfileSelectVisible = false)
    }"""
close_all_sheets_new = """    private fun closeAllSheets() {
        sheetState.value = sheetState.value.closeAll()
    }"""
content = content.replace(close_all_sheets_old, close_all_sheets_new)

# In case it wasn't replaced properly due to spaces, let's use regex
content = re.sub(r'private fun closeAllSheets\(\) \{[^\}]+\}', 'private fun closeAllSheets() {\n        sheetState.value = sheetState.value.closeAll()\n    }', content)

# Fix errorMessage assignment to use string resources
# "导入失败：文件格式不正确或已损坏"
content = content.replace('errorMessage.value = "导入失败：文件格式不正确或已损坏"', 'sheetState.value = sheetState.value.copy(errorMessageResId = com.qiuye.calendarkotlin.R.string.import_failed_invalid_file)')
# clearErrorMessage
content = content.replace('errorMessage.value = null', 'sheetState.value = sheetState.value.copy(errorMessageResId = null)')

import_line = "import androidx.lifecycle.viewmodel.viewModelFactory\nimport com.qiuye.calendarkotlin.R\n"
content = content.replace("import androidx.lifecycle.viewmodel.viewModelFactory\n", import_line)

with open(file_path, "w", encoding="utf-8") as f:
    f.write(content)
