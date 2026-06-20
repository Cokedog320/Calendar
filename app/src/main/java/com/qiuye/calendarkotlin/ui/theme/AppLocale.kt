package com.qiuye.calendarkotlin.ui.theme

import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

fun isEnglishAppLocale(): Boolean {
    val appLocales = AppCompatDelegate.getApplicationLocales()
    if (!appLocales.isEmpty) {
        return appLocales.toLanguageTags()
            .split(",")
            .firstOrNull()
            ?.startsWith("en", ignoreCase = true)
            ?: false
    }
    return Locale.getDefault().language.startsWith("en", ignoreCase = true)
}
