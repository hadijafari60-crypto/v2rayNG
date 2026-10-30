commit: bcd0b5e01fa12062c0b567458e65efe3d4c4fa47
ref: work/home-screen-redesign-2026-10-30
job status: failure
run: https://github.com/hadijafari60-crypto/v2rayNG/actions/runs/41090883746

## Kotlin compile errors (e: lines)
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/main/MainScreen.kt:225:21 No parameter with name 'servers' found.
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/main/MainScreen.kt:226:21 No parameter with name 'selectedGuid' found.
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/main/MainScreen.kt:227:21 No parameter with name 'onSelectServer' found.
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/main/MainScreen.kt:227:40 Cannot infer type for value parameter 'guid'. Specify it explicitly.

## Failed tests

## Test result counts

## Last 40 lines of build.log
> Task :app:desugarFdroidDebugFileDependencies
> Task :app:mergeFdroidDebugNativeLibs
> Task :app:l8DexDesugarLibFdroidDebug
> Task :app:compileFdroidDebugKotlin
> Task :app:mergeExtDexFdroidDebug

> Task :app:compileFdroidDebugKotlin
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/main/MainScreen.kt:225:21 No parameter with name 'servers' found.
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/main/MainScreen.kt:226:21 No parameter with name 'selectedGuid' found.
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/main/MainScreen.kt:227:21 No parameter with name 'onSelectServer' found.
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/main/MainScreen.kt:227:40 Cannot infer type for value parameter 'guid'. Specify it explicitly.

> Task :app:compileFdroidDebugKotlin FAILED

[Incubating] Problems report is available at: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/build/reports/problems/problems-report.html

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':app:compileFdroidDebugKotlin' (registered by plugin 'org.jetbrains.kotlin.android').
> A failure occurred while executing org.jetbrains.kotlin.compilerRunner.btapi.BuildToolsApiCompilationWork
   > Kotlin compiler: NAMED_PARAMETER_NOT_FOUND
       Kotlin compiler error
         No parameter with name 'servers' found.
         Location: /home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/main/MainScreen.kt:225

* Try:
> Run with --stacktrace option to get the stack trace.
> Run with --info or --debug option to get more log output.
> Run with --scan to get full insights from a Build Scan (powered by Develocity).
> Get more help at https://help.gradle.org.

Deprecated Gradle features were used in this build, making it incompatible with Gradle 10.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

For more on this, please refer to https://docs.gradle.org/9.8.2/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.

BUILD FAILED in 1m 47s
30 actionable tasks: 30 executed

## Last 40 lines of test.log
