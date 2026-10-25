commit: eb30b8a56fa599b991d87e1a54e0e3a26926357d
ref: dev
job status: failure
run: https://github.com/hadijafari60-crypto/v2rayNG/actions/runs/40224661570

## Kotlin compile errors (e: lines)

## Failed tests
HealthyGuardTest > deadSelectedServerIgnoresTheCooldown() FAILED
    org.opentest4j.AssertionFailedError at HealthyGuardTest.kt:188
> Task :app:testPlaystoreDebugUnitTest FAILED

## Test result counts
tests="17" skipped="0" failures="1" errors="0"

## Last 40 lines of build.log
> Task :app:mergeProjectDexFdroidDebug

> Task :app:compilePlaystoreDebugKotlin
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/handler/SettingsManager.kt:501:25 'var pinSHA256: String?' is deprecated. Use pinnedCA256.
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/handler/SettingsManager.kt:504:43 'var pinSHA256: String?' is deprecated. Use pinnedCA256.
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/handler/SettingsManager.kt:505:21 'var pinSHA256: String?' is deprecated. Use pinnedCA256.
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/main/MainGroupTab.kt:33:5 'fun ScrollableTabRow(selectedTabIndex: Int, modifier: Modifier = ..., containerColor: Color = ..., contentColor: Color = ..., edgePadding: Dp = ..., indicator: ComposableFunction1<List<TabPosition>, Unit> = ..., divider: ComposableFunction0<Unit> = ..., tabs: ComposableFunction0<Unit>): Unit' is deprecated. Replaced with PrimaryScrollableTabRow and SecondaryScrollableTabRow tab variants.
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/main/MainGroupTab.kt:41:37 'fun Modifier.tabIndicatorOffset(currentTabPosition: TabPosition): Modifier' is deprecated. Solely for use alongside deprecated TabRowDefaults.Indicator method. For recommended PrimaryIndicator and SecondaryIndicator methods, please use TabIndicatorScope.tabIndicatorOffset method.
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/routing/RoutingSettingActivity.kt:385:43 Elvis operator (?:) always returns the left operand of non-nullable type 'Boolean'.
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/server/ServerCustomConfigActivity.kt:140:44 Unnecessary safe call on a non-null receiver of type 'ProfileItem'.
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/server/ServerCustomConfigActivity.kt:142:38 Unnecessary safe call on a non-null receiver of type 'ProfileItem'.
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/server/ServerCustomConfigActivity.kt:143:42 Unnecessary safe call on a non-null receiver of type 'ProfileItem'.
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/util/HttpUtil.kt:128:37 Unnecessary safe call on a non-null receiver of type 'ResponseBody'.
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/util/HttpUtil.kt:196:45 Unnecessary safe call on a non-null receiver of type 'ResponseBody'.
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/util/HttpUtil.kt:292:42 Elvis operator (?:) always returns the left operand of non-nullable type 'ResponseBody'.

> Task :app:packageFdroidDebug
> Task :app:createFdroidDebugApkListingFileRedirect
> Task :app:assembleFdroidDebug
> Task :app:compilePlaystoreDebugJavaWithJavac
> Task :app:processPlaystoreDebugJavaRes
> Task :app:mergePlaystoreDebugJavaResource
> Task :app:dexBuilderPlaystoreDebug
> Task :app:mergeProjectDexPlaystoreDebug
> Task :app:packagePlaystoreDebug
> Task :app:createPlaystoreDebugApkListingFileRedirect
> Task :app:assemblePlaystoreDebug
> Task :app:assembleDebug

[Incubating] Problems report is available at: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/build/reports/problems/problems-report.html

Deprecated Gradle features were used in this build, making it incompatible with Gradle 10.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

For more on this, please refer to https://docs.gradle.org/9.8.1/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.

BUILD SUCCESSFUL in 3m 2s
82 actionable tasks: 82 executed
Consider enabling configuration cache to speed up this build: https://docs.gradle.org/9.8.1/userguide/configuration_cache_enabling.html

## Last 40 lines of test.log
> Task :app:compilePlaystoreDebugKotlin UP-TO-DATE
> Task :app:javaPreCompilePlaystoreDebug UP-TO-DATE
> Task :app:compilePlaystoreDebugJavaWithJavac UP-TO-DATE
> Task :app:prePlaystoreDebugUnitTestBuild UP-TO-DATE
> Task :app:javaPreCompilePlaystoreDebugUnitTest
> Task :app:processPlaystoreDebugJavaRes UP-TO-DATE
> Task :app:bundlePlaystoreDebugClassesToRuntimeJar
> Task :app:bundlePlaystoreDebugClassesToCompileJar
> Task :app:compilePlaystoreDebugUnitTestKotlin
> Task :app:compilePlaystoreDebugUnitTestJavaWithJavac NO-SOURCE
> Task :app:processPlaystoreDebugUnitTestJavaRes

> Task :app:testPlaystoreDebugUnitTest

HealthyGuardTest > deadSelectedServerIgnoresTheCooldown() FAILED
    org.opentest4j.AssertionFailedError at HealthyGuardTest.kt:188

17 tests completed, 1 failed

> Task :app:testPlaystoreDebugUnitTest FAILED

[Incubating] Problems report is available at: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/build/reports/problems/problems-report.html

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':app:testPlaystoreDebugUnitTest'.
> There were failing tests. See the report at: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/build/reports/tests/testPlaystoreDebugUnitTest/index.html

* Try:
> Run with --scan to get full insights from a Build Scan (powered by Develocity).

Deprecated Gradle features were used in this build, making it incompatible with Gradle 10.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

For more on this, please refer to https://docs.gradle.org/9.8.1/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.

BUILD FAILED in 12s
28 actionable tasks: 6 executed, 22 up-to-date
