import re

file_path = "C:/Users/qiuye/Desktop/new_calender/Calendar/app/src/test/java/com/qiuye/calendarkotlin/viewmodel/CalendarViewModelTest.kt"
with open(file_path, "r", encoding="utf-8") as f:
    content = f.read()

# Fix importData calls
content = re.sub(r'viewModel\.importData\(([^,\n]+)\)', r'viewModel.importData(\1, "Imported Profile")', content)

# Fix addNewProfile calls
content = re.sub(r'viewModel\.addNewProfile\(([^,\n]+)\)', r'viewModel.addNewProfile(\1, "New Profile")', content)

# Fix errorMessage to errorMessageResId
content = content.replace('.errorMessage', '.errorMessageResId')

with open(file_path, "w", encoding="utf-8") as f:
    f.write(content)
