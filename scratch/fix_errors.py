import re

# 1. Fix CalendarViewModel.kt
file_path = "C:/Users/qiuye/Desktop/new_calender/Calendar/app/src/main/java/com/qiuye/calendarkotlin/viewmodel/CalendarViewModel.kt"
with open(file_path, "r", encoding="utf-8") as f:
    content = f.read()

# Fix duplicated isProfileSelectVisible
content = content.replace('val isProfileSelectVisible: Boolean = false,\n    val isProfileSelectVisible: Boolean = false,', 'val isProfileSelectVisible: Boolean = false,')

# Fix importFromFile signature
content = content.replace('fun importFromFile(uri: android.net.Uri, context: android.content.Context) {', 'fun importFromFile(uri: android.net.Uri, context: android.content.Context, importedProfileName: String) {')

# Fix ShiftProfile unresolved reference
content = content.replace('val importedProfile = ShiftProfile(', 'val importedProfile = com.qiuye.calendarkotlin.model.ShiftProfile(')
content = content.replace('val newProfile = ShiftProfile(', 'val newProfile = com.qiuye.calendarkotlin.model.ShiftProfile(')

with open(file_path, "w", encoding="utf-8") as f:
    f.write(content)

# 2. Fix CalendarRoute.kt
file_path = "C:/Users/qiuye/Desktop/new_calender/Calendar/app/src/main/java/com/qiuye/calendarkotlin/ui/CalendarRoute.kt"
with open(file_path, "r", encoding="utf-8") as f:
    content = f.read()

# Add context to CalendarScreen
content = content.replace('val snackbarHostState = remember { SnackbarHostState() }', 'val context = androidx.compose.ui.platform.LocalContext.current\n    val snackbarHostState = remember { SnackbarHostState() }')

# Fix addNewProfile method references
content = content.replace('onAddProfile = viewModel::addNewProfile', 'onAddProfile = { name -> viewModel.addNewProfile(name, context.getString(com.qiuye.calendarkotlin.R.string.new_shift_profile)) }')

# Fix importFromFile call
content = content.replace('viewModel.importFromFile(uri, context)', 'viewModel.importFromFile(uri, context, context.getString(com.qiuye.calendarkotlin.R.string.imported_profile))')

with open(file_path, "w", encoding="utf-8") as f:
    f.write(content)

# 3. Fix SettingsBottomSheet.kt
file_path = "C:/Users/qiuye/Desktop/new_calender/Calendar/app/src/main/java/com/qiuye/calendarkotlin/ui/SettingsBottomSheet.kt"
with open(file_path, "r", encoding="utf-8") as f:
    content = f.read()

# Add defaultName before OutlinedButton
content = content.replace('                                OutlinedButton(', '                                val defaultName = androidx.compose.ui.res.stringResource(com.qiuye.calendarkotlin.R.string.new_shift_profile)\n                                OutlinedButton(')

# Use defaultName inside onClick
content = content.replace('onAddProfile(androidx.compose.ui.res.stringResource(com.qiuye.calendarkotlin.R.string.new_shift_profile))', 'onAddProfile(defaultName)')

with open(file_path, "w", encoding="utf-8") as f:
    f.write(content)
