# Calendar | 智能排班备忘日历

[English Version](#english-version) | [中文版](#中文版)

---

## 中文版

一个功能完善的本地 Android 排班日历、日期备注与定时提醒应用。

本项目旨在提供一个真实可用、结构清晰、逻辑严密的本地时间与日程管理工具，优先保证本地可用、提醒可靠、数据稳定。它集成了轮班管理、每日备忘、提醒闹铃等核心日常功能。

### 最近更新 (🌟 Recent Updates)

- 🧳 **日期详情页手动改班新增“出差”（橙色）**：
  - 在点击日期后的手动改班区域新增“出差”选项，位于“休假”旁边，颜色为橙色，便于与常规班次和休假快速区分。
  - 出差支持按天覆盖规律排班，并补充了对应的单元测试覆盖（含常量属性、覆盖优先级与调色板断言）。
- 🎨 **主题三态切换（主页一键）与深色季节高对比优化**：
  - 主页右上角主题按钮升级为三态循环：**跟随系统 → 浅色 → 深色**，并为三种模式提供不同图标（含“跟随系统”专属图标）。
  - 季节强调色支持按明暗模式分别取值；在深色表面（底栏、今日圆圈、选中边框）使用提亮 accent，显著提升秋/冬主题可读性与对比度。
- ✅ **UI 仪器化测试稳定性修复**：
  - 修复 `CalendarUiTest` 中因持久化备注状态导致的用例间相互影响，消除全量跑偶发失败（单跑通过、全量偶发失败）问题。
- 🗓️ **多方案排班管理与独立提醒隔离**：
  - 引入了多套排班方案（Profiles）的创建、切换与删除功能，支持在主界面点击顶部栏标题快速拉起方案切换抽屉。
  - 不同方案下的任务提醒进行按方案 ID 隔离，切换方案时仅显示和响应该方案的专属提醒，切换时系统闹钟自动重新注册。
- 🔄 **全局备忘录共享与备份导入导出兼容**：
  - 将原本分散在各排班方案中的每日备注迁移为“全局共享备注”，切换方案时备注依然保留，避免信息丢失。
  - 增强了数据备份导入导出功能，完美兼容并智能合并老版本单方案备份和新版本多方案备份数据的合并与读取。
- 👑 **主界面标题栏（Top Bar）对齐优化与纯净国际化**：
  - 重构了主页标题栏，主标题直观展示当前处于活动状态的排班方案名，并利用对称平衡（Symmetric Spacer）技术在数学上实现了主标题文本的绝对动态居中，避免了下拉箭头带来的视觉偏斜。
  - 移除了 UI 部分所有硬编码中文，整合入 `strings.xml` 进行完全国际化。
  - `CalendarRepository` 彻底移除硬编码默认参数，规范化了生命周期与测试构造。
- 🌐 **英文界面日期格式优化与六行布局适配**：
  - 顶栏月份由全称改为缩写（如 "September 2026" → "Sep 2026"），消除长月份名对右侧上/下月箭头的挤压。
  - 副标题月份标签保留全称（"September 2026"），兼顾视觉美观与紧凑性。
  - 详情页由数字格式改为美式短格式 + 中点分隔星期（如 "2026-09-21 Sunday" → "Sep 21, 2026 · Sunday"）。
  - 当月历恰好呈六行时，英文胶囊模式下自动收窄日期与胶囊间距（10dp → 4dp），避免底部提示点被挤压；五行及以下布局完全不变。
- 🚀 **冷启动 ANR 风险修复**：
  - 移除 `MainActivity.onCreate` 中的 `runBlocking` 同步阻塞读取，改用 `collectAsState` 以系统当前 locale 作初值异步加载，消除低端机 ANR 隐患。
  - 在 `LaunchedEffect` 内加入相等判断，避免重复调用 `AppCompatDelegate.setApplicationLocales`。
- 📌 **备忘录中心交互重构与置顶头部**：
  - **置顶头部标题栏**：固定了“备忘录中心”的标题与关闭按钮，无论列表如何滚动，用户随时可以方便地点击关闭或划走面板。
  - **卡片直观勾选与删除**：在勾选某条备注左侧的复选框后，卡片右侧会动态显示删除（垃圾桶）按钮，点击可直接进行二次确认删除，无需像以前一样繁琐地滑回屏幕最顶端点击删除。
- ⚡ **提醒保存瞬时返回（消除阻塞）**：
  - 移除了原有的同步等待 Compose Snackbar 消失（约 4 秒）与人为设定的延迟。
  - 引入了非阻塞的 Android 系统原生 `Toast` 提示反馈（在保存成功且存在权限异常时，自动调整为长提示）。
  - 实现点击“保存”后 **0 毫秒延迟** 即刻返回日历主界面，而提示信息仍会悬浮在主屏幕下方。
- 📅 **农历与节日标签清洗规范化**：
  - 规范化了备忘录中心（Notes Center）列表卡片中的农历标签。重构为在单行内优雅地显示农历日期与对应的节日/节气（如 `二月十二 · 消费者权益日`），去除了冗余前缀。
- 🧪 **完整单元测试与集成测试套件**：
  - 构建了涵盖 Domain、Tasks、Data、ViewModel 和 UI 层的全面测试套件，测试总数已达到 70+。
  - 新增了对 `ChineseCalendarInfo.getCleanLunarLabel` 格式化转换器的全方位测试覆盖，包括普通日期、节气和公历节日场景。
  - 新增了 `CalendarRepositoryTest` 覆盖序列化、默认缺省回退、设置/详细更新以及 roundtrip 数据的读写。包括 Room 数据库多版本迁移测试、DataStore Preferences 本地文件持久化验证、ViewModel 状态流响应式 Turbine 断言等。

### 现在能做什么

- **月历视图**：显示公历日期、农历信息（如初一、十五、二十四节气）以及法定节假日和补班标记（“休”/“班”）。
- **智能排班标签**：将自适应排班标签平铺在日期单元格的左上角。在普通日子以饱满的“小印章”形式呈现，而在节假日时自动收缩避让。
- **每日备注**：支持在点击特定日期后添加、修改或查看当天的备忘信息，底部圆点指示备注状态。
- **备忘录搜索中心**：支持纯数字“免破折号”模糊搜索（如输入 `202605` 精准定位至 2026 年 5 月的备注）。
- **定时提醒闹铃**：创建、编辑和删除定时提醒。在提醒时间到达时，即使应用处于后台或重启状态，也会通过系统闹铃和状态栏通知准时进行提示。

### 技术栈

- **语言**: Kotlin
- **UI 框架**: Jetpack Compose, Material Design 3
- **导航**: AndroidX Navigation
- **持久化**: Room Database (SQLite), DataStore Preferences
- **调度器**: AlarmManager (系统精确定时器)
- **构建工具**: Gradle Kotlin DSL

### 主要代码结构

主要代码位于 `app/src/main/java/com/qiuye/calendarkotlin/` 下：
- `ui/`：日历面板、设置页、备忘录中心等 Compose 界面组件
- `viewmodel/`：日历主状态、响应式交互流
- `data/`：日期备注、班次配置等 DataStore Preferences 数据处理
- `domain/`：日期网格计算、农历换算与节假日匹配算法
- `tasks/`：包含闹钟定时调度（AlarmManager）、系统状态栏通知（NotificationCompat）以及提醒的交互设计

### 测试与验证

项目包含完善的单元测试（在 JVM 运行）与仪器化 UI 测试（在真机或模拟器上运行）。

**运行单元测试（JVM）**:
```powershell
.\gradlew.bat testDebugUnitTest
```
**编译仪器化测试 APK**:
```powershell
.\gradlew.bat assembleDebugAndroidTest
```
**在连接的设备上运行仪器化测试**:
```powershell
.\gradlew.bat connectedDebugAndroidTest
```

---

## English Version

A fully-featured local Android Calendar application designed for shift rotation management, daily note taking, and scheduled reminder alerts.

This repository serves as a reliable, clean, and highly robust daily utility tool that prioritizes off-line usability, punctual alarm scheduling, and local data persistence.

### 🌟 Recent Updates

- 🧳 **Added “Business Trip” (Orange) to Manual Shift Override in Day Details**:
  - Added a new **Business Trip** option in the manual shift section opened after tapping a date, next to **Vacation**, with an orange color for quick visual distinction.
  - Business Trip supports per-day override over pattern shifts, with unit-test coverage for constant fields, override precedence, and palette assertions.
- 🎨 **Tri-State Theme Toggle (Home Quick Switch) + Dark Season Contrast Upgrade**:
  - Upgraded the top-right home theme button to a 3-state cycle: **System → Light → Dark**, with distinct icons for each mode (including a dedicated System icon).
  - Season accents now support light/dark variants; dark surfaces (bottom bar, today badge, selected borders) use brighter accents for better readability, especially in Autumn/Winter themes.
- ✅ **Instrumented UI Test Stability Fix**:
  - Fixed state leakage across `CalendarUiTest` cases caused by persisted notes, removing flaky failures that appeared in full-suite runs.
- 🗓️ **Multi-Profile Shift Management & Segregated Reminders**:
  - Introduced creating, switching, and deleting multiple shift profiles. Users can tap the top bar title to invoke the profile switcher drawer.
  - Reminders are now segregated by profile ID, ensuring that only reminders belonging to the active profile are displayed and scheduled via system alarms.
- 🔄 **Global Notes & Backward-Compatible Backup/Restore**:
  - Migrated profile-specific notes to a global structure, ensuring daily notes remain shared across profiles.
  - Upgraded backup import/export code to seamlessly read and merge both legacy single-profile backups and new multi-profile JSON configurations.
- 👑 **Top Bar Alignment Refactoring & Complete i18n**:
  - Redesigned the home screen Top Bar: the main title displays the active profile name, while utilizing a Symmetric Spacer alignment technique to guarantee perfect visual centering regardless of text length.
  - Removed all hardcoded Chinese strings inside UI layers, centralizing them in `strings.xml` for complete i18n.
  - Removed default parameter hardcoding from the `CalendarRepository` constructor.
- 🌐 **English Locale Date Format & Six-Row Layout Adjustments**:
  - Top bar month uses short abbreviation (e.g., "September 2026" → "Sep 2026") to prevent long month names from compressing the prev/next arrow buttons.
  - Subtitle month label retains full name ("September 2026") for visual aesthetics.
  - Detail page switches from numeric format to American short format with a middot separator (e.g., "2026-09-21 Sunday" → "Sep 21, 2026 · Sunday").
  - When the month grid renders exactly six rows, the English capsule layout automatically tightens the spacing between date number and capsule (10dp → 4dp) so status dots at the bottom are not squeezed; five-row and shorter layouts remain unchanged.
- 🚀 **Cold-Start ANR Risk Fix**:
  - Removed the `runBlocking` synchronous blocking read from `MainActivity.onCreate`, switching to `collectAsState` with the current system locale as the initial value for asynchronous loading, eliminating ANR risk on low-end devices.
  - Added an equality check inside `LaunchedEffect` to avoid redundant `AppCompatDelegate.setApplicationLocales` calls.
- 📌 **Notes Center Layout & Deletion Interaction Refactoring**:
  - **Sticky Top Bar**: Extracted the "Notes Center" title and close buttons outside the scrollable `LazyColumn` for easier access.
  - **Localized Checked Deletion**: Checking a note card's left-side selector now dynamically reveals a red delete button on the card itself, enabling instant on-the-spot deletion.
- ⚡ **Instant Return on Save (Zero-Delay Navigation)**:
  - Eliminated coroutine suspension blocks waiting for Snackbar dismissals.
  - Replaced it with non-blocking Android system `Toast` messages.
  - Save action now returns to the calendar main screen immediately with **0ms delay**.
- 📅 **Clean Lunar & Holiday Label Formatting**:
  - Cleaned up lunar holiday labels on note cards, rendering single-line combinations of lunar dates, terms, and holidays (e.g. `二月十二 · 消费者权益日`) without redundant prefixes.
- 🧪 **Comprehensive Test Suite**:
  - Built a robust testing infrastructure covering Domain, Tasks, Data, ViewModels, and UI layers with 70+ verified test cases.
  - Features multi-version Room migration testing, local DataStore Preferences serialization validation (`CalendarRepositoryTest`), Turbine assertions, and Compose UI tests.

### Key Features

- **Monthly Calendar Grid**: View Gregorian calendar days, Lunar dates, and statutory holiday tags (e.g. "休" / "班").
- **Smart Shift Labels**: Adaptive stamp-style shift tags located in the top-left of each day cell. The font and padding automatically adjust to prevent overlapping with holiday tags.
- **Daily Notes**: Write, view, or update quick text memos for any day.
- **Searchable Notes Center**: Search historical notes with simple digits (e.g., searching `202605` or `20265` to find May 2026 memos).
- **Alarm Alerter**: Schedule, edit, or delete reminders. Alarms are registered via the system scheduler and will trigger notifications even after device reboots.

### Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose, Material Design 3
- **Navigation**: AndroidX Navigation
- **Persistence**: Room Database (SQLite), DataStore Preferences
- **Scheduling**: AlarmManager (System Exact Timer)
- **Build Tool**: Gradle Kotlin DSL

### Project Directory Structure

Core source files reside under `app/src/main/java/com/qiuye/calendarkotlin/`:
- `ui/`: Compose layout screens for main view, calendar details, settings, and notes bottom sheet.
- `viewmodel/`: Core state logic and user event mapping.
- `data/`: Local cache managers and DataStore Preference configuration files.
- `domain/`: Math calculators for monthly day grids, Lunar conversion tables, and statutory holiday rules.
- `tasks/`: Scheduled reminders package covering alarm setup, Notification builders, and receiver boot restorations.

### Running Tests

**Run local unit tests (JVM)**:
```powershell
.\gradlew.bat testDebugUnitTest
```
**Assemble android instrumented tests APK**:
```powershell
.\gradlew.bat assembleDebugAndroidTest
```
**Run instrumented tests on connected device**:
```powershell
.\gradlew.bat connectedDebugAndroidTest
```
