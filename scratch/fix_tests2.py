import re

file_path = "C:/Users/qiuye/Desktop/new_calender/Calendar/app/src/test/java/com/qiuye/calendarkotlin/viewmodel/CalendarViewModelTest.kt"
with open(file_path, "r", encoding="utf-8") as f:
    content = f.read()

# Fix errorMessage assertions
content = content.replace('assertEquals("导入失败：文件格式不正确或已损坏", viewModel.uiState.value.errorMessageResId)', 'assertEquals(com.qiuye.calendarkotlin.R.string.import_failed_invalid_file, viewModel.uiState.value.errorMessageResId)')

with open(file_path, "w", encoding="utf-8") as f:
    f.write(content)
