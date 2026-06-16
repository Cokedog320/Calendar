import os

service_file = "C:/Users/qiuye/Desktop/new_calender/Calendar/app/src/main/java/com/qiuye/calendarkotlin/tasks/service/ReminderService.kt"
with open(service_file, "r", encoding="utf-8") as f:
    content = f.read()

# Replace saveReminder signature and splitting logic
old_sig = """    suspend fun saveReminder(
        reminderId: Long?,
        title: String,
        note: String,
        scheduledAtMillis: Long,
        allowPast: Boolean,
        profileId: String? = null
    ): SaveReminderResult {"""

new_sig = """    suspend fun saveReminder(
        reminderId: Long?,
        input: String,
        scheduledAtMillis: Long,
        allowPast: Boolean,
        profileId: String? = null
    ): SaveReminderResult {"""

content = content.replace(old_sig, new_sig)

old_body = """        val trimmedTitle = title.trim()
        if (trimmedTitle.isEmpty()) {
            return SaveReminderResult.ValidationError("标题不能为空")
        }

        val now = System.currentTimeMillis()"""

new_body = """        val lines = input.lines()
        val trimmedTitle = lines.firstOrNull()?.trim().orEmpty()
        val trimmedNote = lines.drop(1).joinToString("\\n").trim()

        if (trimmedTitle.isEmpty()) {
            return SaveReminderResult.ValidationError("标题不能为空")
        }

        val now = System.currentTimeMillis()"""

content = content.replace(old_body, new_body)
content = content.replace("note = note.trim()", "note = trimmedNote")

with open(service_file, "w", encoding="utf-8") as f:
    f.write(content)


vm_file = "C:/Users/qiuye/Desktop/new_calender/Calendar/app/src/main/java/com/qiuye/calendarkotlin/tasks/ui/viewmodel/TasksViewModel.kt"
with open(vm_file, "r", encoding="utf-8") as f:
    vm_content = f.read()

old_vm_sig = """    suspend fun saveReminder(
        reminderId: Long?,
        title: String,
        note: String,
        dateStartMillis: Long,
        minutesOfDay: Int,
        allowPast: Boolean
    ): SaveReminderResult {"""

new_vm_sig = """    suspend fun saveReminder(
        reminderId: Long?,
        input: String,
        dateStartMillis: Long,
        minutesOfDay: Int,
        allowPast: Boolean
    ): SaveReminderResult {"""

vm_content = vm_content.replace(old_vm_sig, new_vm_sig)
vm_content = vm_content.replace("service.saveReminder(reminderId, title, note, scheduledAtMillis, allowPast, activeProfileId)", 
                                "service.saveReminder(reminderId, input, scheduledAtMillis, allowPast, activeProfileId)")

with open(vm_file, "w", encoding="utf-8") as f:
    f.write(vm_content)


ui_file = "C:/Users/qiuye/Desktop/new_calender/Calendar/app/src/main/java/com/qiuye/calendarkotlin/tasks/ui/screen/EditTaskScreen.kt"
with open(ui_file, "r", encoding="utf-8") as f:
    ui_content = f.read()

ui_content = ui_content.replace("onSave: suspend (Long?, String, String, Long, Int, Boolean) -> SaveReminderResult", 
                                "onSave: suspend (Long?, String, Long, Int, Boolean) -> SaveReminderResult")

old_ui_save = """        val lines = title.lines()
        val titleToSave = lines.firstOrNull()?.trim().orEmpty()
        val noteToSave = lines.drop(1).joinToString("\\n").trim()

        val result = onSave(
            reminderId,
            titleToSave,
            noteToSave,
            dateStartMillis,
            minutesOfDay,
            allowPast
        )"""

new_ui_save = """        val result = onSave(
            reminderId,
            title,
            dateStartMillis,
            minutesOfDay,
            allowPast
        )"""

ui_content = ui_content.replace(old_ui_save, new_ui_save)

with open(ui_file, "w", encoding="utf-8") as f:
    f.write(ui_content)

main_file = "C:/Users/qiuye/Desktop/new_calender/Calendar/app/src/main/java/com/qiuye/calendarkotlin/MainActivity.kt"
with open(main_file, "r", encoding="utf-8") as f:
    main_content = f.read()

main_content = main_content.replace("onSave = { reminderId, title, note, dateStartMillis, minutesOfDay, allowPast ->", 
                                    "onSave = { reminderId, input, dateStartMillis, minutesOfDay, allowPast ->")
main_content = main_content.replace("title = title,\n                        note = note,", "input = input,")

main_content = main_content.replace("onSave = { editedReminderId, title, note, dateStartMillis, minutesOfDay, allowPast ->", 
                                    "onSave = { editedReminderId, input, dateStartMillis, minutesOfDay, allowPast ->")

with open(main_file, "w", encoding="utf-8") as f:
    f.write(main_content)
