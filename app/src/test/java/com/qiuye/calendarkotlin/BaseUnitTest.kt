package com.qiuye.calendarkotlin

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.Locale

/**
 * 基础单元测试类，配置了 Robolectric 和 AndroidJUnit4 运行器。
 * 建议涉及到 Android 框架类（如 Context, Intent 等）的测试继承此类。
 * 默认强制中文环境，确保 isEnglishAppLocale() 返回 false。
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [34], manifest = Config.NONE)
abstract class BaseUnitTest {
    @get:org.junit.Rule
    val mainDispatcherRule = com.qiuye.calendarkotlin.utils.MainDispatcherRule()

    @Before
    open fun setUpLocale() {
        Locale.setDefault(Locale.CHINA)
    }
}
