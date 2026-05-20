# Calendar

一个本地 Android 日历应用，用来管理排班日历、日期备注和定时提醒。

这个项目最初是一个个人日历练手应用，后来把提醒功能合并进来，逐步变成一个更完整的本地时间管理工具。它不是一个商业化产品仓库，也不打算包装成大型开源项目；这里更关注真实可用、结构清楚、方便继续迭代。

## 现在能做什么

- 查看月历，并显示公历日期、农历信息和节假日标记
- 设置排班循环，查看每天对应的班次
- 给具体日期添加备注
- 在备注中心搜索、回看已有备注
- 创建、编辑、删除提醒
- 在提醒中心查看全部提醒，并按状态筛选
- 到点后通过系统通知提醒
- App 重启或手机重启后恢复未完成提醒

## 功能说明

### 日历

主界面以月历为核心。日期格子会展示当天的基础信息，并标记是否有备注或提醒。点击日期后，可以查看当天详情；如果当天已有备注，会自动打开详情，方便继续查看或修改。

### 备注

备注按日期保存，适合记录当天的班次说明、临时事项或简单备忘。备注中心主要用于搜索和回看，不承担复杂待办管理。

### 提醒

提醒是独立的数据模块，支持标题、备注、提醒日期和提醒时间。保存提醒后，应用会通过系统闹钟安排通知；提醒触发后可以从通知进入对应提醒详情。

提醒依赖 Android 的通知权限和精确闹钟权限。权限未开启时，应用会尽量给出提示，但最终是否准时弹出通知仍取决于系统设置。

## 技术栈

- Kotlin
- Jetpack Compose
- AndroidX Navigation
- DataStore Preferences
- Room
- AlarmManager
- Gradle Kotlin DSL

## 项目结构

```text
.
├── app/        Android 应用源码、资源、测试和 Room schema
├── docs/       测试记录、回归检查和观察笔记
├── gradle/     Gradle wrapper 配置
└── README.md   项目说明
```

主要代码在 `app/src/main/java/com/qiuye/calendarkotlin/` 下：

- `ui/`：日历、备注中心、提醒中心等 Compose 界面
- `viewmodel/`：日历状态和交互逻辑
- `data/`：日历备注、排班配置等本地数据
- `domain/`：日期计算、农历和节假日相关逻辑
- `tasks/`：提醒的数据、调度、通知和编辑页面

## 构建方式

项目使用 Android Studio 打开即可。也可以在项目根目录运行：

```powershell
.\gradlew.bat assembleDebug
```

生成的 debug APK 位于：

```text
app/build/outputs/apk/debug/app-debug.apk
```

## 测试

常用检查命令：

```powershell
.\gradlew.bat testDebugUnitTest
```

```powershell
.\gradlew.bat assembleDebugAndroidTest
```

如果要在真机或模拟器上跑仪器测试，可以连接设备后运行：

```powershell
.\gradlew.bat connectedDebugAndroidTest
```

提醒功能建议最终以真机测试为准，尤其是退出 App 后触发提醒、通知权限、精确闹钟权限和重启后恢复提醒这些场景。

## 权限说明

应用会用到以下权限：

- `POST_NOTIFICATIONS`：Android 13 及以上发送通知需要
- `SCHEDULE_EXACT_ALARM`：用于尽量准时触发提醒
- `RECEIVE_BOOT_COMPLETED`：手机重启后恢复未完成提醒

不同品牌系统可能还会有额外的后台限制。如果提醒没有触发，需要检查通知权限、精确闹钟权限、电池优化和后台限制。

## 当前定位

Calendar 是一个偏个人使用的原生 Android 应用。它优先保证本地可用、提醒可靠、数据不乱迁移，不追求复杂账号系统、云同步或 Web 端形态。

后续如果继续扩展，比较适合围绕这些方向做小步迭代：

- 优化提醒列表和日期详情之间的联动
- 增强备注搜索和筛选
- 补充更多回归测试
- 改善不同 Android 版本上的提醒权限提示
