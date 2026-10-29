package com.v2ray.ang.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    isRunning: Boolean,
    isRefreshing: Boolean,
    hasInternet: Boolean,
    onMainButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val waitingForConnection = isRefreshing && !isRunning
    val internetLost = isRunning && !hasInternet
    val buttonLabel = when {
        internetLost -> "NO INTERNET"
        isRunning -> "CONNECTED"
        waitingForConnection -> "CONNECTING"
        else -> "CONNECT"
    }
    val statusText = when {
        internetLost -> "Internet connection is unavailable. Tests are paused. Tap to disconnect the VPN."
        isRunning -> "VPN is connected. Tap the button to disconnect."
        waitingForConnection -> "Finding and connecting to the fastest healthy server…"
        else -> "Tap to find and connect to a healthy server."
    }
    val buttonColor = when {
        internetLost -> MaterialTheme.colorScheme.errorContainer
        isRunning -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.secondaryContainer
    }
    val buttonTextColor = when {
        internetLost -> MaterialTheme.colorScheme.onErrorContainer
        isRunning -> MaterialTheme.colorScheme.onPrimary
        else -> MaterialTheme.colorScheme.onSecondaryContainer
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = when {
                internetLost -> "Internet connection lost"
                isRunning -> "You’re protected"
                waitingForConnection -> "Preparing your connection"
                else -> "Ready to connect"
            },
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.size(32.dp))

        Box(
            modifier = Modifier.size(236.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(204.dp)
                    .clip(CircleShape)
                    .background(buttonColor)
                    .clickable(
                        onClick = onMainButtonClick,
                    )
                    .semantics { contentDescription = buttonLabel },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = buttonLabel,
                    color = buttonTextColor,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
            if (waitingForConnection) {
                CircularProgressIndicator(
                    modifier = Modifier.size(232.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 3.dp,
                )
            }
        }

        Spacer(Modifier.size(28.dp))
        Text(
            text = statusText,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
