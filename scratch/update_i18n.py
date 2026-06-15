import re

# 1. Update CalendarRepository.kt
file_path = "C:/Users/qiuye/Desktop/new_calender/Calendar/app/src/main/java/com/qiuye/calendarkotlin/data/CalendarRepository.kt"
with open(file_path, "r", encoding="utf-8") as f:
    content = f.read()

content = content.replace('class CalendarRepository(', 'class CalendarRepository(\n    private val defaultProfileName: String = "默认方案",\n')
content = content.replace('name = "默认方案"', 'name = defaultProfileName')

with open(file_path, "w", encoding="utf-8") as f:
    f.write(content)

# 2. Update TasksGraph.kt
file_path = "C:/Users/qiuye/Desktop/new_calender/Calendar/app/src/main/java/com/qiuye/calendarkotlin/tasks/TasksGraph.kt"
with open(file_path, "r", encoding="utf-8") as f:
    content = f.read()

content = content.replace(
    'calendarRepository ?: CalendarRepository(appContext.calendarDataStore)',
    'calendarRepository ?: CalendarRepository(defaultProfileName = appContext.getString(com.qiuye.calendarkotlin.R.string.default_profile_name), dataStore = appContext.calendarDataStore)'
)
with open(file_path, "w", encoding="utf-8") as f:
    f.write(content)

# 3. Update CalendarModels.kt
file_path = "C:/Users/qiuye/Desktop/new_calender/Calendar/app/src/main/java/com/qiuye/calendarkotlin/model/CalendarModels.kt"
with open(file_path, "r", encoding="utf-8") as f:
    content = f.read()

# Just replace "默认方案" with "Default" or empty, but let's change it to "Default" and UI can show whatever it wants if it's new. Wait, if I change it to "Default" it breaks the test? The test expects "默认方案". So I'll leave CalendarModels.kt hardcoded or change it to "Default Profile". Let's change it to "Default Profile" and fix tests if needed. Or just leave CalendarModels.kt alone and it will be overridden by CalendarRepository anyway. Actually CalendarModels.kt has `name = "默认方案"`.
content = content.replace('"默认方案"', '"Default"')

with open(file_path, "w", encoding="utf-8") as f:
    f.write(content)

# 4. Update CalendarViewModel.kt
file_path = "C:/Users/qiuye/Desktop/new_calender/Calendar/app/src/main/java/com/qiuye/calendarkotlin/viewmodel/CalendarViewModel.kt"
with open(file_path, "r", encoding="utf-8") as f:
    content = f.read()

content = content.replace('fun importDataFromFile(uri: android.net.Uri, context: android.content.Context) {', 'fun importDataFromFile(uri: android.net.Uri, context: android.content.Context, importedProfileName: String) {')
content = content.replace('importData(jsonStr)', 'importData(jsonStr, importedProfileName)')
content = content.replace('internal suspend fun importData(jsonString: String) {', 'internal suspend fun importData(jsonString: String, importedProfileName: String) {')
content = content.replace('name = "导入的方案",', 'name = importedProfileName,')

content = content.replace('fun addNewProfile(name: String) {', 'fun addNewProfile(name: String, defaultName: String) {')
content = content.replace('name = name.trim().ifBlank { "新排班方案" }', 'name = name.trim().ifBlank { defaultName }')
content = content.replace('com.qiuye.calendarkotlin.model.ShiftProfile', 'ShiftProfile')

with open(file_path, "w", encoding="utf-8") as f:
    f.write(content)

# 5. Update CalendarRoute.kt
file_path = "C:/Users/qiuye/Desktop/new_calender/Calendar/app/src/main/java/com/qiuye/calendarkotlin/ui/CalendarRoute.kt"
with open(file_path, "r", encoding="utf-8") as f:
    content = f.read()

content = content.replace('viewModel.importDataFromFile(uri, context)', 'viewModel.importDataFromFile(uri, context, context.getString(com.qiuye.calendarkotlin.R.string.imported_profile))')
content = content.replace('uiState.errorMessage?.let { message ->', 'uiState.errorMessageResId?.let { resId ->\n            val message = context.getString(resId)')
content = content.replace('LaunchedEffect(uiState.errorMessage)', 'LaunchedEffect(uiState.errorMessageResId)')
content = content.replace('onAddProfile = { viewModel.addNewProfile(it) }', 'onAddProfile = { viewModel.addNewProfile(it, context.getString(com.qiuye.calendarkotlin.R.string.new_shift_profile)) }')

with open(file_path, "w", encoding="utf-8") as f:
    f.write(content)

# 6. Update SettingsBottomSheet.kt
file_path = "C:/Users/qiuye/Desktop/new_calender/Calendar/app/src/main/java/com/qiuye/calendarkotlin/ui/SettingsBottomSheet.kt"
with open(file_path, "r", encoding="utf-8") as f:
    content = f.read()

content = content.replace('onAddProfile("新排班方案")', 'onAddProfile(androidx.compose.ui.res.stringResource(com.qiuye.calendarkotlin.R.string.new_shift_profile))')
with open(file_path, "w", encoding="utf-8") as f:
    f.write(content)

