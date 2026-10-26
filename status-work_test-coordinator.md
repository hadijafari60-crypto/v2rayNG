commit: 2f7a6ca73ae457f7e41f4f7ba924d2ae77739a02
ref: work/test-coordinator
job status: failure
run: https://github.com/hadijafari60-crypto/v2rayNG/actions/runs/40259653478

## Kotlin compile errors (e: lines)
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/test/java/com/v2ray/ang/ui/main/MainTestRequestsTest.kt:42:51 Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type 'MainTestRequests.Bulk?'.
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/test/java/com/v2ray/ang/ui/main/MainTestRequestsTest.kt:45:50 Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type 'MainTestRequests.Bulk?'.
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/test/java/com/v2ray/ang/ui/main/MainTestRequestsTest.kt:47:60 Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type 'MainTestRequests.Bulk?'.
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/test/java/com/v2ray/ang/ui/main/MainTestRequestsTest.kt:48:49 Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type 'MainTestRequests.Bulk?'.
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/test/java/com/v2ray/ang/ui/main/MainTestRequestsTest.kt:59:54 Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type 'MainTestRequests.Bulk?'.

## Failed tests
> Task :app:compilePlaystoreDebugUnitTestKotlin FAILED

## Test result counts

## Last 40 lines of build.log
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

> Task :app:compileFdroidDebugJavaWithJavac
> Task :app:processFdroidDebugJavaRes
> Task :app:mergeFdroidDebugJavaResource
> Task :app:dexBuilderFdroidDebug
> Task :app:mergeProjectDexFdroidDebug
> Task :app:compilePlaystoreDebugJavaWithJavac
> Task :app:processPlaystoreDebugJavaRes
> Task :app:mergePlaystoreDebugJavaResource
> Task :app:dexBuilderPlaystoreDebug
> Task :app:mergeProjectDexPlaystoreDebug
> Task :app:packageFdroidDebug
> Task :app:createFdroidDebugApkListingFileRedirect
> Task :app:assembleFdroidDebug
> Task :app:packagePlaystoreDebug
> Task :app:createPlaystoreDebugApkListingFileRedirect
> Task :app:assemblePlaystoreDebug
> Task :app:assembleDebug

[Incubating] Problems report is available at: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/build/reports/problems/problems-report.html

Deprecated Gradle features were used in this build, making it incompatible with Gradle 10.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

For more on this, please refer to https://docs.gradle.org/9.8.2/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.

BUILD SUCCESSFUL in 2m 22s
82 actionable tasks: 82 executed
Consider enabling configuration cache to speed up this build: https://docs.gradle.org/9.8.2/userguide/configuration_cache_enabling.html

## Last 40 lines of test.log
> Task :app:compilePlaystoreDebugJavaWithJavac UP-TO-DATE
> Task :app:prePlaystoreDebugUnitTestBuild UP-TO-DATE
> Task :app:processPlaystoreDebugJavaRes UP-TO-DATE
> Task :app:javaPreCompilePlaystoreDebugUnitTest
> Task :app:bundlePlaystoreDebugClassesToRuntimeJar
> Task :app:bundlePlaystoreDebugClassesToCompileJar

> Task :app:compilePlaystoreDebugUnitTestKotlin FAILED
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/test/java/com/v2ray/ang/ui/main/MainTestRequestsTest.kt:42:51 Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type 'MainTestRequests.Bulk?'.
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/test/java/com/v2ray/ang/ui/main/MainTestRequestsTest.kt:45:50 Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type 'MainTestRequests.Bulk?'.
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/test/java/com/v2ray/ang/ui/main/MainTestRequestsTest.kt:47:60 Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type 'MainTestRequests.Bulk?'.
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/test/java/com/v2ray/ang/ui/main/MainTestRequestsTest.kt:48:49 Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type 'MainTestRequests.Bulk?'.
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/test/java/com/v2ray/ang/ui/main/MainTestRequestsTest.kt:59:54 Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type 'MainTestRequests.Bulk?'.

[Incubating] Problems report is available at: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/build/reports/problems/problems-report.html

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':app:compilePlaystoreDebugUnitTestKotlin' (registered by plugin 'org.jetbrains.kotlin.android').
> A failure occurred while executing org.jetbrains.kotlin.compilerRunner.btapi.BuildToolsApiCompilationWork
   > Kotlin compiler: UNSAFE_CALL
       Kotlin compiler error
         Only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiver of type 'MainTestRequests.Bulk?'.
         Location: /home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/test/java/com/v2ray/ang/ui/main/MainTestRequestsTest.kt:42

* Try:
> Run with --stacktrace option to get the stack trace.
> Run with --info or --debug option to get more log output.
> Run with --scan to get full insights from a Build Scan (powered by Develocity).
> Get more help at https://help.gradle.org.

Deprecated Gradle features were used in this build, making it incompatible with Gradle 10.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

For more on this, please refer to https://docs.gradle.org/9.8.2/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.

BUILD FAILED in 5s
26 actionable tasks: 4 executed, 22 up-to-date
