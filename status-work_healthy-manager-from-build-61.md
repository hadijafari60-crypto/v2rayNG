commit: 5718205c1ff8455ae40d826d4af3f1a6c324b569
ref: work/healthy-manager-from-build-61
job status: failure
run: https://github.com/hadijafari60-crypto/v2rayNG/actions/runs/40397440875

## Kotlin compile errors (e: lines)
e: file:///home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/main/MainViewModel.kt:148:16 Class '<anonymous>' is not abstract and does not implement abstract member:

## Failed tests

## Test result counts

## Last 40 lines of build.log
> A failure occurred while executing org.jetbrains.kotlin.compilerRunner.btapi.BuildToolsApiCompilationWork
   > Kotlin compiler: ABSTRACT_MEMBER_NOT_IMPLEMENTED
       Kotlin compiler error
         Class '<anonymous>' is not abstract and does not implement abstract member:
         fun copyText(label: String, text: String): Unit
         Location: /home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/main/MainViewModel.kt:148

* Try:
> Run with --stacktrace option to get the stack trace.
> Run with --info or --debug option to get more log output.
> Run with --scan to get full insights from a Build Scan (powered by Develocity).
> Get more help at https://help.gradle.org.
==============================================================================

2: Task failed with an exception.
-----------
* What went wrong:
Execution failed for task ':app:compileFdroidDebugKotlin' (registered by plugin 'org.jetbrains.kotlin.android').
> A failure occurred while executing org.jetbrains.kotlin.compilerRunner.btapi.BuildToolsApiCompilationWork
   > Kotlin compiler: ABSTRACT_MEMBER_NOT_IMPLEMENTED
       Kotlin compiler error
         Class '<anonymous>' is not abstract and does not implement abstract member:
         fun copyText(label: String, text: String): Unit
         Location: /home/runner/work/v2rayNG/v2rayNG/V2rayNG/app/src/main/java/com/v2ray/ang/ui/main/MainViewModel.kt:148

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
