commit: 94a342a08b0f0e239ee8163e8df726bda401ee94
ref: work/healthy-manager-from-build-61
job status: failure
run: https://github.com/hadijafari60-crypto/v2rayNG/actions/runs/40421932358

## Kotlin compile errors (e: lines)
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/main/HealthyManager.kt:136:30 Suspend function 'hasInternet' cannot override non-suspend function 'fun hasInternet(): Boolean' defined in 'com.v2ray.ang.ui.main.GuardPorts'.
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/main/HealthyManager.kt:30:40 'public' function exposes its 'internal' parameter type containing declaration 'MainTestRequests'.
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/main/MainViewModel.kt:264:34 Suspend function 'hasInternet' cannot override non-suspend function 'fun hasInternet(): Boolean' defined in 'com.v2ray.ang.ui.main.HealthyHost'.

## Failed tests

## Test result counts

## Last 40 lines of build.log
* What went wrong:
Execution failed for task ':app:compileFdroidDebugKotlin' (registered by plugin 'org.jetbrains.kotlin.android').
> A failure occurred while executing org.jetbrains.kotlin.compilerRunner.btapi.BuildToolsApiCompilationWork
   > Kotlin compiler: EXPOSED_PARAMETER_TYPE
       Kotlin compiler error
         'public' function exposes its 'internal' parameter type containing declaration 'MainTestRequests'.
         Location: /home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/main/HealthyManager.kt:30

* Try:
> Run with --stacktrace option to get the stack trace.
> Run with --info or --debug option to get more log output.
> Run with --scan to get full insights from a Build Scan (powered by Develocity).
> Get more help at https://help.gradle.org.
==============================================================================

2: Task failed with an exception.
-----------
* What went wrong:
Execution failed for task ':app:compilePlaystoreDebugKotlin' (registered by plugin 'org.jetbrains.kotlin.android').
> A failure occurred while executing org.jetbrains.kotlin.compilerRunner.btapi.BuildToolsApiCompilationWork
   > Kotlin compiler: EXPOSED_PARAMETER_TYPE
       Kotlin compiler error
         'public' function exposes its 'internal' parameter type containing declaration 'MainTestRequests'.
         Location: /home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/main/HealthyManager.kt:30

* Try:
> Run with --stacktrace option to get the stack trace.
> Run with --info or --debug option to get more log output.
> Run with --scan to get full insights from a Build Scan (powered by Develocity).
> Get more help at https://help.gradle.org.
==============================================================================

Deprecated Gradle features were used in this build, making it incompatible with Gradle 10.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

For more on this, please refer to https://docs.gradle.org/9.8.2/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.

BUILD FAILED in 1m 58s
68 actionable tasks: 68 executed

## Last 40 lines of test.log
