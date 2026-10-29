package com.v2ray.ang.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
    val waitingForConnection = isRefreshing && !isRunning && hasInternet
    val internetLost = !hasInternet
    val buttonLabel = when {
        internetLost -> "NO INTERNET"
        isRunning -> "CONNECTED"
        waitingForConnection -> "CONNECTING"
        else -> "CONNECT"
    }
    val headline = when {
        internetLost && isRunning -> "Internet connection lost"
        internetLost -> "No internet connection"
        isRunning -> "You’re protected"
        waitingForConnection -> "Preparing your connection"
        else -> "Ready to connect"
    }
    val statusText = when {
        internetLost && isRunning -> "Your VPN is still active, but your device has no internet access. Restore internet, or disconnect the VPN."
        internetLost -> "Your device has no internet access. Restore internet before connecting."
        isRunning -> "Your VPN connection is active. Tap the button whenever you want to disconnect."
        waitingForConnection -> "Checking healthy servers and selecting a fast connection…"
        else -> "Healthy Manager will look for a responsive server and connect automatically."
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
    val statusLabel = when {
        internetLost -> "INTERNET UNAVAILABLE"
        isRunning -> "VPN CONNECTED"
        waitingForConnection -> "SEARCHING FOR A SERVER"
        else -> "READY"
    }
    val statusColor = when {
        internetLost -> MaterialTheme.colorScheme.error
        isRunning -> MaterialTheme.colorScheme.primary
        waitingForConnection -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "HEALTHY MANAGER",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.size(10.dp))
        Text(
            text = headline,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = "A faster connection, chosen automatically",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.size(28.dp))

        Box(
            modifier = Modifier.size(236.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(204.dp)
                    .clip(CircleShape)
                    .background(buttonColor)
                    .clickable(onClick = onMainButtonClick)
                    .semantics { contentDescription = buttonLabel },
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = buttonLabel,
                        color = buttonTextColor,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    if (isRunning && !internetLost) {
                        Spacer(Modifier.size(4.dp))
                        Text(
                            text = "Tap to disconnect",
                            color = buttonTextColor,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
            }
            if (waitingForConnection) {
                CircularProgressIndicator(
                    modifier = Modifier.size(232.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 3.dp,
                )
            }
        }

        Spacer(Modifier.size(24.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
            ),
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "CONNECTION STATUS",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                    )
                    Surface(
                        color = statusColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(50),
                    ) {
                        Text(
                            text = statusLabel,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = statusColor,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
