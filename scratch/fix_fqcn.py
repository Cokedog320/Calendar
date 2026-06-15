import os

vm_file = "C:/Users/qiuye/Desktop/new_calender/Calendar/app/src/main/java/com/qiuye/calendarkotlin/viewmodel/CalendarViewModel.kt"
with open(vm_file, "r", encoding="utf-8") as f:
    content = f.read()

if "import com.qiuye.calendarkotlin.model.ShiftProfile" not in content:
    content = content.replace("import com.qiuye.calendarkotlin.model.ShiftDefinition", 
                              "import com.qiuye.calendarkotlin.model.ShiftDefinition\nimport com.qiuye.calendarkotlin.model.ShiftProfile")

content = content.replace("com.qiuye.calendarkotlin.model.ShiftProfile", "ShiftProfile")

with open(vm_file, "w", encoding="utf-8") as f:
    f.write(content)
