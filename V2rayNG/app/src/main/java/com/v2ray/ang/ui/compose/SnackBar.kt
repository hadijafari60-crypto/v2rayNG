package com.v2ray.ang.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.v2ray.ang.extension.cancelFallbackToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch

enum class ToastType {
    NORMAL, SUCCESS, ERROR, INFO
}

data class AppSnackbarMessage(
    val message: CharSequence,
    val type: ToastType = ToastType.NORMAL,
    val long: Boolean = false,
)

object AppSnackbarManager {
    private val _messages = MutableSharedFlow<AppSnackbarMessage>(
        replay = 0,
        extraBufferCapacity = 32,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val messages = _messages.asSharedFlow()

    fun hasActiveHost(): Boolean = _messages.subscriptionCount.value > 0

    fun show(
        message: CharSequence,
        type: ToastType = ToastType.NORMAL,
        long: Boolean = false,
    ): Boolean {
        if (!hasActiveHost()) return false
        return _messages.tryEmit(
            AppSnackbarMessage(
                message = message,
                type = type,
                long = long
            )
        )
    }
}

class AppSnackbarController(
    val hostState: SnackbarHostState,
    private val scope: CoroutineScope,
) {
    // Replace an active snackbar so urgent feedback is not delayed behind a message backlog.
    private val presenter = SnackbarLatestPresenter(scope)

    fun show(message: CharSequence, type: ToastType = ToastType.NORMAL, long: Boolean = false) {
        hostState.currentSnackbarData?.dismiss()
        presenter.show {
            hostState.showSnackbar(
                AppSnackbarVisuals(
                    message = message.toString(),
                    type = type,
                    duration = if (long) SnackbarDuration.Long else SnackbarDuration.Short
                )
            )
        }
    }
}

private data class AppSnackbarVisuals(
    override val message: String,
    val type: ToastType,
    override val duration: SnackbarDuration,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false
) : SnackbarVisuals

val LocalAppSnackbar = staticCompositionLocalOf<AppSnackbarController> {
    error("AppSnackbarController not provided. Wrap your content in AppTheme.")
}

@Composable
fun rememberAppSnackbarController(): AppSnackbarController {
    val hostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    return remember(hostState, scope) { AppSnackbarController(hostState, scope) }
}

@Composable
fun AppSnackbarBridge(
    controller: AppSnackbarController
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(controller, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            AppSnackbarManager.messages
                .onSubscription { cancelFallbackToast() }
                .collect { event ->
                controller.show(
                    message = event.message,
                    type = event.type,
                    long = event.long
                )
            }
        }
    }
}

private val ToastCornerRadius = 24.dp
private val ToastHorizontalPad = 16.dp
private val ToastVerticalPad = 12.dp
private const val ToastMaxLines = 8
private const val ToastMaxWidthFraction = 0.75f
private val ToastBottomOffset = 100.dp

@Composable
fun AppSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier) {
        // Render only the current item without Material3's animated cross-fade. Replacing the
        // current message must never draw an outgoing gray snackbar over the incoming green one.
        val data = hostState.currentSnackbarData
        val maxSnackbarWidth = maxWidth * ToastMaxWidthFraction
        val density = LocalDensity.current
        val navigationBarHeight = with(density) {
            WindowInsets.navigationBars.getBottom(this).toDp()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = ToastBottomOffset + navigationBarHeight),
            contentAlignment = Alignment.BottomCenter
        ) {
            if (data != null) {
                val type = (data.visuals as? AppSnackbarVisuals)?.type ?: ToastType.NORMAL
                val isDark = LocalDarkTheme.current
                val bgColor = when (type) {
                    ToastType.NORMAL -> if (isDark) toastNormalBgDark else toastNormalBgLight
                    ToastType.SUCCESS -> toastSuccessBg
                    ToastType.ERROR -> toastErrorBg
                    ToastType.INFO -> toastInfoBg
                }

                Surface(
                    modifier = Modifier
                        .wrapContentWidth()
                        .widthIn(max = maxSnackbarWidth),
                    shape = RoundedCornerShape(ToastCornerRadius),
                    color = bgColor,
                    shadowElevation = 0.dp,
                ) {
                    Row(
                        modifier = Modifier.padding(
                            horizontal = ToastHorizontalPad,
                            vertical = ToastVerticalPad
                        ),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = data.visuals.message,
                            color = toastTextColor,
                            fontSize = 14.sp,
                            maxLines = ToastMaxLines,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.wrapContentWidth()
                        )
                    }
                }
            }
        }
    }
}
