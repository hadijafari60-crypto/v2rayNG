commit: b079d397dc844e996612388ce1812cec8845a352
ref: 11/merge
job status: success
run: https://github.com/hadijafari60-crypto/v2rayNG/actions/runs/40306890242

## Kotlin compile errors (e: lines)

## Failed tests

## Test result counts
tests="2" skipped="0" failures="0" errors="0"
tests="20" skipped="0" failures="0" errors="0"
tests="5" skipped="0" failures="0" errors="0"
tests="5" skipped="0" failures="0" errors="0"

## Last 40 lines of build.log
> Task :app:mergeFdroidDebugJavaResource
> Task :app:dexBuilderFdroidDebug
> Task :app:mergeProjectDexFdroidDebug
> Task :app:packageFdroidDebug
> Task :app:createFdroidDebugApkListingFileRedirect
> Task :app:assembleFdroidDebug

> Task :app:compilePlaystoreDebugKotlin
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/handler/SettingsManager.kt:501:25 'var pinSHA256: String?' is deprecated. Use pinnedCA256.
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/handler/SettingsManager.kt:504:43 'var pinSHA256: String?' is deprecated. Use pinnedCA256.
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/handler/SettingsManager.kt:505:21 'var pinSHA256: String?' is deprecated. Use pinnedCA256.
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/routing/RoutingSettingActivity.kt:385:43 Elvis operator (?:) always returns the left operand of non-nullable type 'Boolean'.
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/server/ServerCustomConfigActivity.kt:140:44 Unnecessary safe call on a non-null receiver of type 'ProfileItem'.
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/server/ServerCustomConfigActivity.kt:142:38 Unnecessary safe call on a non-null receiver of type 'ProfileItem'.
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/server/ServerCustomConfigActivity.kt:143:42 Unnecessary safe call on a non-null receiver of type 'ProfileItem'.
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/util/HttpUtil.kt:129:37 Unnecessary safe call on a non-null receiver of type 'ResponseBody'.
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/util/HttpUtil.kt:202:45 Unnecessary safe call on a non-null receiver of type 'ResponseBody'.
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/util/HttpUtil.kt:298:42 Elvis operator (?:) always returns the left operand of non-nullable type 'ResponseBody'.

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

For more on this, please refer to https://docs.gradle.org/9.8.2/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.

BUILD SUCCESSFUL in 3m 16s
82 actionable tasks: 82 executed
Consider enabling configuration cache to speed up this build: https://docs.gradle.org/9.8.2/userguide/configuration_cache_enabling.html

## Last 40 lines of test.log
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
> Task :app:javaPreCompilePlaystoreDebugUnitTest
> Task :app:processPlaystoreDebugJavaRes UP-TO-DATE
> Task :app:bundlePlaystoreDebugClassesToRuntimeJar
> Task :app:bundlePlaystoreDebugClassesToCompileJar

> Task :app:compilePlaystoreDebugUnitTestKotlin
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/test/java/com/v2ray/ang/ui/compose/SnackbarLatestPresenterTest.kt:30:9 This declaration needs opt-in. Its usage should be marked with '@kotlinx.coroutines.ExperimentalCoroutinesApi' or '@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)'
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/test/java/com/v2ray/ang/ui/compose/SnackbarLatestPresenterTest.kt:37:9 This declaration needs opt-in. Its usage should be marked with '@kotlinx.coroutines.ExperimentalCoroutinesApi' or '@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)'
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/test/java/com/v2ray/ang/ui/compose/SnackbarLatestPresenterTest.kt:63:9 This declaration needs opt-in. Its usage should be marked with '@kotlinx.coroutines.ExperimentalCoroutinesApi' or '@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)'
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/test/java/com/v2ray/ang/ui/compose/SnackbarLatestPresenterTest.kt:67:9 This declaration needs opt-in. Its usage should be marked with '@kotlinx.coroutines.ExperimentalCoroutinesApi' or '@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)'
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/test/java/com/v2ray/ang/ui/compose/SnackbarLatestPresenterTest.kt:71:9 This declaration needs opt-in. Its usage should be marked with '@kotlinx.coroutines.ExperimentalCoroutinesApi' or '@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)'
w: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/test/java/com/v2ray/ang/ui/compose/SnackbarLatestPresenterTest.kt:72:9 This declaration needs opt-in. Its usage should be marked with '@kotlinx.coroutines.ExperimentalCoroutinesApi' or '@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)'

> Task :app:compilePlaystoreDebugUnitTestJavaWithJavac NO-SOURCE
> Task :app:processPlaystoreDebugUnitTestJavaRes
> Task :app:testPlaystoreDebugUnitTest

[Incubating] Problems report is available at: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/build/reports/problems/problems-report.html

Deprecated Gradle features were used in this build, making it incompatible with Gradle 10.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

For more on this, please refer to https://docs.gradle.org/9.8.2/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.

BUILD SUCCESSFUL in 11s
28 actionable tasks: 6 executed, 22 up-to-date
Consider enabling configuration cache to speed up this build: https://docs.gradle.org/9.8.2/userguide/configuration_cache_enabling.html
