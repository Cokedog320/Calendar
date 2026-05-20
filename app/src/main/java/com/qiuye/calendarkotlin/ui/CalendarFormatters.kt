package com.qiuye.calendarkotlin.ui

import java.time.format.DateTimeFormatter
import java.util.Locale

internal val monthFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年 M月", Locale.CHINA)
internal val fullDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 EEEE", Locale.CHINA)
internal val shortDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.CHINA)
internal val noteMonthFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月", Locale.CHINA)
internal val noteDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("M月d日 EEEE", Locale.CHINA)
internal val pickerMonthFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月", Locale.CHINA)
internal val weekdayLabels = listOf("一", "二", "三", "四", "五", "六", "日")


