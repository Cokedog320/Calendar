# Pager 稳定性观察

适用场景：月历已切换为 `HorizontalPager` 后的真机交互回归。

## 先盯什么

- 月份标题是否只在页面真正停稳后切换，避免滑动中来回跳。
- 左右连续滑月时，是否出现“页码回弹”“卡在半页”“滑完后月份不一致”。
- 点顶部箭头和手势滑动交替使用时，是否出现状态互相拉扯。
- 打开日详情、设置、备注页后，再滑月是否还会误触翻页。
- 跳转到远月后，是否能稳定落在目标月，不闪回到旧月。

## 最小 adb 观察

```powershell
adb devices
adb shell dumpsys activity activities | findstr com.qiuye.calendarkotlin
adb logcat -d -t 200 AndroidRuntime:E ActivityTaskManager:I ActivityManager:I Choreographer:W *:S
```

## 异常信号

- `AndroidRuntime` 出现 `FATAL EXCEPTION`。
- `ActivityTaskManager` / `ActivityManager` 出现连续重启、ANR、`waited`、`not responding`。
- `Choreographer` 持续大量 `Skipped frames`，并且和滑月操作同步出现。
- 页面切换后标题月份与当前页明显不一致。

## 快速判定

- 通过：连续滑月、箭头切月、今天、跳转日期、打开底部 sheet 后再返回，均无崩溃、无明显卡顿、无错月。
- 需要回看：只要出现一次标题错月、页回弹、底部 sheet 误触翻页，就先保留现场日志和复现步骤。
