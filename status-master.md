commit: 6285b73b16f3d930e7017e8188e9936eac1d9f1f
ref: master
job status: success
run: https://github.com/hadijafari60-crypto/v2rayNG/actions/runs/40241356576

## Kotlin compile errors (e: lines)

## Failed tests

## Test result counts
tests="17" skipped="0" failures="0" errors="0"

## Last 40 lines of build.log
> Task :app:mergeProjectDexFdroidDebug
> Task :app:packageFdroidDebug
> Task :app:createFdroidDebugApkListingFileRedirect
> Task :app:assembleFdroidDebug

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

BUILD SUCCESSFUL in 3m
82 actionable tasks: 82 executed
Consider enabling configuration cache to speed up this build: https://docs.gradle.org/9.8.1/userguide/configuration_cache_enabling.html

## Last 40 lines of test.log
> Task :app:generatePlaystoreDebugResValues UP-TO-DATE
> Task :app:mapPlaystoreDebugSourceSetPaths UP-TO-DATE
> Task :app:processPlaystoreDebugNavigationResources UP-TO-DATE
> Task :app:compilePlaystoreDebugNavigationResources UP-TO-DATE
> Task :app:extractPlaystoreDebugSupportedLocales UP-TO-DATE
> Task :app:generatePlaystoreDebugLocaleConfig UP-TO-DATE
> Task :app:generatePlaystoreDebugResources UP-TO-DATE
> Task :app:mergePlaystoreDebugResources UP-TO-DATE
> Task :app:packagePlaystoreDebugResources UP-TO-DATE
> Task :app:parsePlaystoreDebugLocalResources UP-TO-DATE
> Task :app:createPlaystoreDebugCompatibleScreenManifests UP-TO-DATE
> Task :app:extractDeepLinksPlaystoreDebug UP-TO-DATE
> Task :app:processPlaystoreDebugMainManifest UP-TO-DATE
> Task :app:processPlaystoreDebugManifest UP-TO-DATE
> Task :app:processPlaystoreDebugManifestForPackage UP-TO-DATE
> Task :app:processPlaystoreDebugResources UP-TO-DATE
> Task :app:compilePlaystoreDebugKotlin UP-TO-DATE
> Task :app:javaPreCompilePlaystoreDebug UP-TO-DATE
> Task :app:compilePlaystoreDebugJavaWithJavac UP-TO-DATE
> Task :app:prePlaystoreDebugUnitTestBuild UP-TO-DATE
> Task :app:processPlaystoreDebugJavaRes UP-TO-DATE
> Task :app:javaPreCompilePlaystoreDebugUnitTest
> Task :app:bundlePlaystoreDebugClassesToRuntimeJar
> Task :app:bundlePlaystoreDebugClassesToCompileJar
> Task :app:compilePlaystoreDebugUnitTestKotlin
> Task :app:compilePlaystoreDebugUnitTestJavaWithJavac NO-SOURCE
> Task :app:processPlaystoreDebugUnitTestJavaRes
> Task :app:testPlaystoreDebugUnitTest

[Incubating] Problems report is available at: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/build/reports/problems/problems-report.html

Deprecated Gradle features were used in this build, making it incompatible with Gradle 10.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

For more on this, please refer to https://docs.gradle.org/9.8.1/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.

BUILD SUCCESSFUL in 12s
28 actionable tasks: 6 executed, 22 up-to-date
Consider enabling configuration cache to speed up this build: https://docs.gradle.org/9.8.1/userguide/configuration_cache_enabling.html
