package com.qiuye.calendarkotlin

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.compose.ui.test.swipeRight
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertTrue
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

class CalendarUiTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    private val monthFormatter = DateTimeFormatter.ofPattern("yyyy年 M月", Locale.CHINA)

    @Test
    fun pagerSwipeLeftMonthAdvances() {
        val currentMonth = YearMonth.now()
        val nextMonth = currentMonth.plusMonths(1)

        rule.onNodeWithTag("month_title")
            .assertTextEquals(currentMonth.format(monthFormatter))

        rule.onNodeWithTag("calendar_pager")
            .performTouchInput { swipeLeft() }

        rule.waitForIdle()

        rule.onNodeWithTag("month_title")
            .assertTextEquals(nextMonth.format(monthFormatter))
    }

    @Test
    fun pagerSwipeRightMonthGoesBack() {
        val currentMonth = YearMonth.now()
        val prevMonth = currentMonth.minusMonths(1)

        rule.onNodeWithTag("calendar_pager")
            .performTouchInput { swipeRight() }

        rule.waitForIdle()

        rule.onNodeWithTag("month_title")
            .assertTextEquals(prevMonth.format(monthFormatter))
    }

    @Test
    fun todayButtonReturnsToCurrentMonth() {
        val currentMonth = YearMonth.now()

        repeat(3) {
            rule.onNodeWithTag("calendar_pager")
                .performTouchInput { swipeLeft() }
            rule.waitForIdle()
        }

        rule.onNodeWithTag("btn_today").performClick()
        rule.waitForIdle()

        rule.onNodeWithTag("month_title")
            .assertTextEquals(currentMonth.format(monthFormatter))
    }

    @Test
    fun sheetsAreMutuallyExclusive() {
        rule.onNodeWithTag("btn_settings").performClick()
        rule.waitForIdle()
        rule.onNodeWithTag("sheet_settings").assertIsDisplayed()
        assertTrue(rule.onAllNodesWithTag("sheet_settings").fetchSemanticsNodes().isNotEmpty())
        assertTrue(rule.onAllNodesWithTag("sheet_notes").fetchSemanticsNodes().isEmpty())
        assertTrue(rule.onAllNodesWithTag("sheet_day_detail").fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun clickDateSelectsAndDetailButtonOpensDaySheet() {
        val targetDate = LocalDate.now().withDayOfMonth(15)

        rule.onNodeWithTag("day_cell_$targetDate").performClick()
        rule.waitForIdle()

        assertTrue(rule.onAllNodesWithTag("sheet_day_detail").fetchSemanticsNodes().isEmpty())
        rule.onNodeWithTag("btn_day_detail").performClick()
        rule.waitForIdle()

        rule.onNodeWithTag("sheet_day_detail").assertIsDisplayed()
        assertTrue(rule.onAllNodesWithTag("sheet_settings").fetchSemanticsNodes().isEmpty())
        assertTrue(rule.onAllNodesWithTag("sheet_notes").fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun saveDayDetailDismissesSheet() {
        val targetDate = LocalDate.now().withDayOfMonth(15)

        rule.onNodeWithTag("day_cell_$targetDate").performClick()
        rule.waitForIdle()
        rule.onNodeWithTag("btn_day_detail").performClick()
        rule.waitForIdle()
        rule.onNodeWithTag("sheet_day_detail").assertIsDisplayed()

        rule.onNodeWithTag("input_day_note").performTextInput("保存后关闭")
        rule.onNodeWithTag("btn_day_save").performClick()
        rule.waitUntil(timeoutMillis = 5_000) {
            rule.onAllNodesWithTag("sheet_day_detail").fetchSemanticsNodes().isEmpty()
        }

        assertTrue(rule.onAllNodesWithTag("sheet_day_detail").fetchSemanticsNodes().isEmpty())
    }

    @Test
    fun dateJumpWheelConfirmSelectsVisibleDate() {
        val currentMonth = YearMonth.now()

        rule.onNodeWithTag("btn_date_jump").performClick()
        rule.waitForIdle()

        rule.onNodeWithTag("wheel_year").assertIsDisplayed()
        rule.onNodeWithTag("wheel_month").assertIsDisplayed()
        rule.onNodeWithTag("wheel_day").assertIsDisplayed()
        rule.onNodeWithTag("btn_date_confirm").performClick()
        rule.waitForIdle()

        rule.onNodeWithTag("month_title")
            .assertTextEquals(currentMonth.format(monthFormatter))
    }

    @Test
    fun settingsSaveUpdatesMonthAndDismissesSheet() {
        val currentMonth = YearMonth.now()

        rule.onNodeWithTag("calendar_pager")
            .performTouchInput { swipeLeft() }
        rule.waitForIdle()

        rule.onNodeWithTag("btn_settings").performClick()
        rule.waitForIdle()
        rule.onNodeWithTag("sheet_settings").assertIsDisplayed()

        rule.onNodeWithTag("field_cycle_start_date_open").performClick()
        rule.waitForIdle()
        rule.onNodeWithTag("wheel_year").assertIsDisplayed()
        rule.onNodeWithTag("btn_date_confirm").performClick()
        rule.waitForIdle()

        rule.onNodeWithTag("sheet_settings").performScrollToNode(hasTestTag("btn_settings_save"))
        rule.onNodeWithTag("btn_settings_save").performClick()
        rule.waitUntil(timeoutMillis = 5_000) {
            rule.onAllNodesWithTag("sheet_settings").fetchSemanticsNodes().isEmpty()
        }

        rule.onNodeWithTag("month_title")
            .assertTextEquals(currentMonth.format(monthFormatter))
    }

    @Test
    fun notesSearchAndSelectClosesSheet() {
        val targetDate = LocalDate.now().withDayOfMonth(15)
        val noteText = "备注回归测试"

        rule.onNodeWithTag("day_cell_$targetDate").performClick()
        rule.waitForIdle()
        rule.onNodeWithTag("btn_day_detail").performClick()
        rule.waitForIdle()
        rule.onNodeWithTag("input_day_note").performTextClearance()
        rule.onNodeWithTag("input_day_note").performTextInput(noteText)
        rule.onNodeWithTag("btn_day_save").performClick()
        rule.waitUntil(timeoutMillis = 5_000) {
            rule.onAllNodesWithTag("sheet_day_detail").fetchSemanticsNodes().isEmpty()
        }

        rule.onNodeWithTag("btn_notes").performClick()
        rule.waitForIdle()
        rule.onNodeWithTag("sheet_notes").assertIsDisplayed()
        rule.onNodeWithTag("input_notes_search").performTextInput(noteText)
        rule.onNodeWithTag("note_item_$targetDate").assertIsDisplayed()
        rule.onNodeWithTag("note_item_$targetDate").performClick()
        rule.waitUntil(timeoutMillis = 5_000) {
            rule.onAllNodesWithTag("sheet_notes").fetchSemanticsNodes().isEmpty()
        }
    }

    @Test
    fun clickDateWithExistingNoteOpensDayDetailSheet() {
        val targetDate = LocalDate.now().withDayOfMonth(16)
        val noteText = "已有备注自动打开"

        rule.onNodeWithTag("day_cell_$targetDate").performClick()
        rule.waitForIdle()
        rule.onNodeWithTag("btn_day_detail").performClick()
        rule.waitForIdle()
        rule.onNodeWithTag("input_day_note").performTextInput(noteText)
        rule.onNodeWithTag("btn_day_save").performClick()
        rule.waitUntil(timeoutMillis = 5_000) {
            rule.onAllNodesWithTag("sheet_day_detail").fetchSemanticsNodes().isEmpty()
        }

        rule.onNodeWithTag("day_cell_$targetDate").performClick()
        rule.waitForIdle()

        rule.onNodeWithTag("sheet_day_detail").assertIsDisplayed()
    }

    @Test
    fun rapidSwipeThenTodayNoCrash() {
        repeat(5) {
            rule.onNodeWithTag("calendar_pager")
                .performTouchInput { swipeLeft() }
        }

        rule.onNodeWithTag("btn_today").performClick()
        rule.waitForIdle()

        val currentMonth = YearMonth.now()
        rule.onNodeWithTag("month_title")
            .assertTextEquals(currentMonth.format(monthFormatter))
        assertTrue(rule.onAllNodesWithTag("calendar_pager").fetchSemanticsNodes().isNotEmpty())
    }
}


