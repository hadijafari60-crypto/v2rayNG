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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.v2ray.ang.dto.TrafficStatsUiState
import com.v2ray.ang.dto.entities.ServersCache
import com.v2ray.ang.extension.toSpeedString

@Composable
fun HomeScreen(
    isRunning: Boolean,
    isRefreshing: Boolean,
    hasInternet: Boolean,
    trafficStats: TrafficStatsUiState,
    servers: List<ServersCache>,
    selectedGuid: String?,
    onMainButtonClick: () -> Unit,
    onSelectServer: (String) -> Unit,
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
    val selectedServer = servers.firstOrNull { it.guid == selectedGuid }
    var showServerPicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "HEALTHY MANAGER",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = headline,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.size(6.dp))
        Text(
            text = "A faster connection, chosen automatically",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.size(24.dp))

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

        Spacer(Modifier.size(22.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
            ),
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "CURRENT SERVER",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = selectedServer?.profile?.remarks?.ifBlank { "Server not selected" }
                                ?: "Server not selected",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                        )
                    }
                    Text(
                        text = selectedServer?.testDelayMillis
                            ?.takeIf { it > 0L }
                            ?.let { "$it ms" } ?: "— ms",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    TrafficMetric(
                        title = "UPLOAD SPEED",
                        value = trafficStats.uploadBytesPerSecond.toSpeedString(),
                        modifier = Modifier.weight(1f),
                    )
                    TrafficMetric(
                        title = "DOWNLOAD SPEED",
                        value = trafficStats.downloadBytesPerSecond.toSpeedString(),
                        modifier = Modifier.weight(1f),
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    TrafficMetric(
                        title = "UPLOADED",
                        value = formatTrafficBytes(trafficStats.uploadedBytes),
                        modifier = Modifier.weight(1f),
                    )
                    TrafficMetric(
                        title = "DOWNLOADED",
                        value = formatTrafficBytes(trafficStats.downloadedBytes),
                        modifier = Modifier.weight(1f),
                    )
                }
                TextButton(
                    onClick = { showServerPicker = true },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("CHANGE SERVER")
                }
            }
        }
    }

    if (showServerPicker) {
        val topServers = remember(servers) {
            servers.asSequence()
                .filter { it.testDelayMillis > 0L }
                .sortedBy { it.testDelayMillis }
                .take(5)
                .toList()
        }
        AlertDialog(
            onDismissRequest = { showServerPicker = false },
            title = { Text("Choose a server") },
            text = {
                if (topServers.isEmpty()) {
                    Text("No tested servers are available yet. Start a connection test first.")
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        topServers.forEachIndexed { index, server ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        showServerPicker = false
                                        onSelectServer(server.guid)
                                    },
                                color = if (server.guid == selectedGuid) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surface
                                },
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = "${index + 1}. ${server.profile.remarks.ifBlank { "Server" }}",
                                        modifier = Modifier.weight(1f),
                                        maxLines = 1,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                    Text(
                                        text = "${server.testDelayMillis} ms",
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showServerPicker = false }) { Text("CLOSE") }
            },
        )
    }
}

@Composable
private fun TrafficMetric(title: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

private fun formatTrafficBytes(bytes: Long): String {
    if (bytes < 1024L) return "$bytes B"
    val units = arrayOf("KB", "MB", "GB", "TB")
    var value = bytes.toDouble()
    var unit = -1
    do {
        value /= 1024.0
        unit++
    } while (value >= 1024.0 && unit < units.lastIndex)
    return String.format(java.util.Locale.ROOT, "%.1f %s", value, units[unit])
}
