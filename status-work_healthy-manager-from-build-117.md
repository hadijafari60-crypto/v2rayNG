commit: 891d8d8bb5d11334eaedc033d3ecd4ac4df507e7
ref: work/healthy-manager-from-build-117
job status: failure
run: https://github.com/hadijafari60-crypto/v2rayNG/actions/runs/40867893585

## Kotlin compile errors (e: lines)
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/main/MainScreen.kt:222:33 Unresolved reference 'isRefreshing'.

## Failed tests

## Test result counts

## Last 40 lines of build.log
> Task :app:compressFdroidDebugAssets
> Task :app:processFdroidDebugResources
> Task :app:generateFdroidDebugGlobalSynthetics
> Task :app:checkFdroidDebugDuplicateClasses
> Task :app:mergeLibDexFdroidDebug
> Task :app:desugarFdroidDebugFileDependencies
> Task :app:mergeFdroidDebugJniLibFolders
> Task :app:l8DexDesugarLibFdroidDebug
> Task :app:compileFdroidDebugKotlin
> Task :app:mergeExtDexFdroidDebug

> Task :app:compileFdroidDebugKotlin FAILED
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/main/MainScreen.kt:222:33 Unresolved reference 'isRefreshing'.

[Incubating] Problems report is available at: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/build/reports/problems/problems-report.html

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':app:compileFdroidDebugKotlin' (registered by plugin 'org.jetbrains.kotlin.android').
> A failure occurred while executing org.jetbrains.kotlin.compilerRunner.btapi.BuildToolsApiCompilationWork
   > Kotlin compiler: UNRESOLVED_REFERENCE
       Kotlin compiler error
         Unresolved reference 'isRefreshing'.
         Location: /home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/main/MainScreen.kt:222

* Try:
> Run with --stacktrace option to get the stack trace.
> Run with --info or --debug option to get more log output.
> Run with --scan to get full insights from a Build Scan (powered by Develocity).
> Get more help at https://help.gradle.org.

Deprecated Gradle features were used in this build, making it incompatible with Gradle 10.

You can use '--warning-mode all' to show the individual deprecation warnings and determine if they come from your own scripts or plugins.

For more on this, please refer to https://docs.gradle.org/9.8.2/userguide/command_line_interface.html#sec:command_line_warnings in the Gradle documentation.

BUILD FAILED in 1m 27s
29 actionable tasks: 29 executed

## Last 40 lines of test.log
