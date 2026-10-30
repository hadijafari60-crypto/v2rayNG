package com.v2ray.ang.ui.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.v2ray.ang.dto.TrafficStatsUiState
import com.v2ray.ang.dto.entities.ServersCache
import com.v2ray.ang.extension.toSpeedString

private val HomeBackground = Color(0xFF050A12)
private val HomeBlue = Color(0xFF087BA8)
private val HomeBlueLight = Color(0xFFD9F0FF)
private val HomeText = Color(0xFF102033)
private val HomeMuted = Color(0xFF7189A5)
private val HomeGreen = Color(0xFF0BA878)

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
    val selectedServer = servers.firstOrNull { it.guid == selectedGuid }
    val rankedServers = remember(servers) {
        servers.asSequence()
            .filter { it.testDelayMillis > 0L }
            .sortedBy { it.testDelayMillis }
            .toList()
    }
    val selectedRank = rankedServers.indexOfFirst { it.guid == selectedGuid }
        .takeIf { it >= 0 }?.plus(1)
    var showServerPicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(HomeBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp, vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = headline,
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFFF4F8FF),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(18.dp))

        // Layered translucent rings recreate the soft illuminated power control.
        Box(
            modifier = Modifier
                .size(278.dp)
                .semantics { contentDescription = buttonLabel },
            contentAlignment = Alignment.Center,
        ) {
            Box(
                Modifier
                    .size(278.dp)
                    .clip(CircleShape)
                    .background(Color(0x1A42BFFF))
            )
            Box(
                Modifier
                    .size(252.dp)
                    .clip(CircleShape)
                    .background(Color(0x3342BFFF))
            )
            Box(
                Modifier
                    .size(224.dp)
                    .shadow(34.dp, CircleShape, clip = false)
                    .clip(CircleShape)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF1689B8), Color(0xFF00516F))
                        )
                    )
                    .clickable(onClick = onMainButtonClick),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "⏻",
                        color = Color.White,
                        fontSize = 76.sp,
                        lineHeight = 82.sp,
                        fontWeight = FontWeight.Light,
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = buttonLabel,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                    )
                }
            }
            if (waitingForConnection) {
                CircularProgressIndicator(
                    modifier = Modifier.size(270.dp),
                    color = Color(0xFF79D8FF),
                    strokeWidth = 3.dp,
                )
            }
        }
        if (isRunning && !internetLost) {
            Spacer(Modifier.height(2.dp))
            Surface(
                color = Color(0xFFEAF5FF),
                shape = RoundedCornerShape(30.dp),
                shadowElevation = 8.dp,
            ) {
                Text(
                    text = "Tap to disconnect",
                    modifier = Modifier.padding(horizontal = 22.dp, vertical = 8.dp),
                    color = HomeMuted,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        } else {
            Spacer(Modifier.height(18.dp))
        }

        Spacer(Modifier.height(28.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(22.dp, RoundedCornerShape(30.dp), clip = false),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            border = BorderStroke(1.dp, Color(0xFFD9E9F7)),
        ) {
            Column(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFFF9FCFF), Color(0xFFEAF4FC))
                        )
                    )
                    .padding(horizontal = 22.dp, vertical = 22.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(66.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(Color(0xFFDCEEFF)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("▤", color = HomeBlue, fontSize = 38.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Current Server",
                            color = HomeMuted,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(Modifier.height(3.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = selectedServer?.profile?.remarks?.ifBlank { "Server not selected" }
                                    ?: "Server not selected",
                                color = HomeText,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                            )
                            if (selectedRank != null) {
                                Spacer(Modifier.width(8.dp))
                                Surface(
                                    color = Color(0xFFC9F7E8),
                                    shape = CircleShape,
                                ) {
                                    Text(
                                        "#$selectedRank",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        color = HomeGreen,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.labelLarge,
                                    )
                                }
                            }
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("▮▮▮", color = HomeGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(
                            selectedServer?.testDelayMillis?.takeIf { it > 0L }?.let { "$it ms" } ?: "— ms",
                            color = HomeGreen,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                HomeDivider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    TrafficMetric(
                        title = "UPLOAD SPEED",
                        value = trafficStats.uploadBytesPerSecond.toSpeedString(),
                        symbol = "↑",
                        symbolColor = HomeBlue,
                        modifier = Modifier.weight(1f),
                    )
                    MetricDivider()
                    TrafficMetric(
                        title = "DOWNLOAD SPEED",
                        value = trafficStats.downloadBytesPerSecond.toSpeedString(),
                        symbol = "↓",
                        symbolColor = HomeGreen,
                        modifier = Modifier.weight(1f),
                    )
                }
                HomeDivider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    TrafficMetric(
                        title = "UPLOADED",
                        value = formatTrafficBytes(trafficStats.uploadedBytes),
                        symbol = "⇧",
                        symbolColor = Color(0xFF635BDB),
                        modifier = Modifier.weight(1f),
                    )
                    MetricDivider()
                    TrafficMetric(
                        title = "DOWNLOADED",
                        value = formatTrafficBytes(trafficStats.downloadedBytes),
                        symbol = "⇩",
                        symbolColor = HomeGreen,
                        modifier = Modifier.weight(1f),
                    )
                }

                Spacer(Modifier.height(2.dp))
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showServerPicker = true },
                    shape = RoundedCornerShape(22.dp),
                    color = Color(0xFFD9EDFF),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 17.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text("⇄", color = HomeBlue, fontSize = 27.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(14.dp))
                        Text(
                            "Change Server",
                            color = HomeBlue,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(Modifier.width(12.dp))
                        Text("›", color = HomeBlue, fontSize = 34.sp, lineHeight = 34.sp)
                    }
                }
            }
        }
        Spacer(Modifier.height(18.dp))
    }

    if (showServerPicker) {
        val topServers = rankedServers.take(5)
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
private fun HomeDivider() {
    Box(
        Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color(0xFFD7E7F5))
    )
}

@Composable
private fun MetricDivider() {
    Box(
        Modifier
            .width(1.dp)
            .height(76.dp)
            .background(Color(0xFFD5E6F4))
    )
}

@Composable
private fun TrafficMetric(
    title: String,
    value: String,
    symbol: String,
    symbolColor: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(symbolColor.copy(alpha = 0.13f)),
            contentAlignment = Alignment.Center,
        ) {
            Text(symbol, color = symbolColor, fontSize = 27.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(10.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = title,
                color = HomeMuted,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = value,
                color = HomeText,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
            )
        }
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
