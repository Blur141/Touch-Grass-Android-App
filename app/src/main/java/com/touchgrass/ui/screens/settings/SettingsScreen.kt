package com.touchgrass.ui.screens.settings

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.touchgrass.data.datastore.NotificationStyle
import com.touchgrass.ui.components.GlassmorphicCard
import com.touchgrass.ui.theme.*

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val prefs by viewModel.preferences.collectAsStateWithLifecycle()
    val installedApps by viewModel.installedApps.collectAsStateWithLifecycle()
    val searchQuery by viewModel.appSearchQuery.collectAsStateWithLifecycle()
    val nameEditValue by viewModel.nameEditValue.collectAsStateWithLifecycle()
    val profileName by viewModel.profileName.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var showNameDialog by remember { mutableStateOf(false) }

    val filteredApps = remember(installedApps, searchQuery) {
        if (searchQuery.isBlank()) installedApps
        else installedApps.filter { it.appName.contains(searchQuery, ignoreCase = true) }
    }
    val blockedApps = remember(filteredApps, prefs.blockedApps) {
        filteredApps.filter { it.packageName in prefs.blockedApps }
    }
    val unblockedApps = remember(filteredApps, prefs.blockedApps) {
        filteredApps.filter { it.packageName !in prefs.blockedApps }
    }

    if (showNameDialog) {
        NameEditDialog(
            currentName = nameEditValue,
            onValueChange = viewModel::setNameEditValue,
            onConfirm = {
                viewModel.saveName(nameEditValue)
                showNameDialog = false
            },
            onDismiss = { showNameDialog = false },
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp),
        ) {
            // ── Header ──────────────────────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = TextPrimary)
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Settings",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                    )
                }
            }

            // ── Profile / Name ───────────────────────────────────────────────────
            item {
                SettingsSection(title = "Profile") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column {
                            Text(
                                if (profileName.isBlank()) "Your Name" else profileName,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = if (profileName.isBlank()) TextMuted else TextPrimary,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                                ),
                            )
                            Text(
                                "Tap the pencil to change your name",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted),
                            )
                        }
                        IconButton(onClick = {
                            viewModel.setNameEditValue(profileName)
                            showNameDialog = true
                        }) {
                            Icon(Icons.Default.Edit, null, tint = GrassGreen)
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(12.dp)) }

            // ── Screen Time Goal ─────────────────────────────────────────────────
            item {
                SettingsSection(title = "Screen Time Goal") {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        Text(
                            text = "Daily limit: ${prefs.dailyGoalMinutes / 60}h ${prefs.dailyGoalMinutes % 60}m",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                        )
                        Slider(
                            value = prefs.dailyGoalMinutes.toFloat(),
                            onValueChange = { viewModel.setDailyGoal(it.toInt()) },
                            valueRange = 30f..480f,
                            steps = 29,
                            colors = SliderDefaults.colors(
                                thumbColor = GrassGreen,
                                activeTrackColor = GrassGreen,
                            ),
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text("30m", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                            Text("8h", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(12.dp)) }

            // ── Appearance ───────────────────────────────────────────────────────
            item {
                SettingsSection(title = "Appearance") {
                    ToggleRow(
                        "Dark Mode", "Keep it dark. It's better.",
                        prefs.isDarkMode, viewModel::setDarkMode,
                    )
                    ToggleRow(
                        "Haptic Feedback", "Feel the XP gains.",
                        prefs.isHapticEnabled, viewModel::setHapticEnabled,
                    )
                }
            }

            item { Spacer(Modifier.height(12.dp)) }

            // ── Notifications ────────────────────────────────────────────────────
            item {
                SettingsSection(title = "Notifications") {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        Text(
                            "Notification Style",
                            style = MaterialTheme.typography.labelMedium.copy(color = TextMuted),
                        )
                        Spacer(Modifier.height(8.dp))
                        NotificationStyle.values().forEach { style ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                RadioButton(
                                    selected = prefs.notificationStyle == style,
                                    onClick = { viewModel.setNotificationStyle(style) },
                                    colors = RadioButtonDefaults.colors(selectedColor = GrassGreen),
                                )
                                Spacer(Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = style.name.lowercase().replaceFirstChar { it.uppercase() },
                                        style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary),
                                    )
                                    Text(
                                        text = when (style) {
                                            NotificationStyle.FUNNY -> "\"Bro you've been scrolling for 2 hours\""
                                            NotificationStyle.MOTIVATIONAL -> "\"Every minute offline is a victory.\""
                                            NotificationStyle.MINIMAL -> "Just screen time. No commentary."
                                        },
                                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted),
                                    )
                                }
                            }
                        }
                    }
                    ToggleRow(
                        "Plant Notifications", "Let your plant speak to you.",
                        prefs.showPetNotifications, viewModel::setShowPetNotifications,
                    )
                }
            }

            item { Spacer(Modifier.height(12.dp)) }

            // ── Sleep Schedule ───────────────────────────────────────────────────
            item {
                SettingsSection(title = "Sleep Schedule") {
                    ToggleRow(
                        "Enable Sleep Reminders",
                        "Get notified at bedtime and wake-up.",
                        prefs.sleepEnabled,
                        viewModel::setSleepEnabled,
                    )
                    if (prefs.sleepEnabled) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = DarkDivider,
                        )
                        TimePickerRow(
                            label = "Bedtime",
                            emoji = "😴",
                            hour = prefs.bedtimeHour,
                            minute = prefs.bedtimeMinute,
                            context = context,
                            onTimePicked = viewModel::setBedtime,
                        )
                        TimePickerRow(
                            label = "Wake Up",
                            emoji = "☀️",
                            hour = prefs.wakeHour,
                            minute = prefs.wakeMinute,
                            context = context,
                            onTimePicked = viewModel::setWakeTime,
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(12.dp)) }

            // ── Blocked Apps ─────────────────────────────────────────────────────
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "App Block List",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = TextMuted, fontWeight = FontWeight.SemiBold,
                        ),
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                    Text(
                        text = "${prefs.blockedApps.size} blocked  •  30s cooldown before bypass",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted),
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setAppSearchQuery(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                "Search apps...",
                                style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted),
                            )
                        },
                        leadingIcon = { Icon(Icons.Default.Search, null, tint = TextMuted) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GrassGreen,
                            unfocusedBorderColor = DarkDivider,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            cursorColor = GrassGreen,
                        ),
                        singleLine = true,
                    )
                }
            }

            // Blocked section header
            if (blockedApps.isNotEmpty()) {
                item {
                    AppSectionLabel(
                        label = "Blocked (${blockedApps.size})",
                        color = AccentRed,
                    )
                }
                items(blockedApps, key = { "blocked_${it.packageName}" }) { app ->
                    AppBlockRow(
                        app = app,
                        isBlocked = true,
                        onToggle = { viewModel.toggleBlockedApp(app.packageName) },
                    )
                }
            }

            // Not blocked section header
            if (unblockedApps.isNotEmpty()) {
                item {
                    AppSectionLabel(
                        label = if (blockedApps.isEmpty()) "All Apps" else "Not Blocked (${unblockedApps.size})",
                        color = TextMuted,
                    )
                }
                items(unblockedApps, key = { "unblocked_${it.packageName}" }) { app ->
                    AppBlockRow(
                        app = app,
                        isBlocked = false,
                        onToggle = { viewModel.toggleBlockedApp(app.packageName) },
                    )
                }
            }

            if (installedApps.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        GlassmorphicCard {
                            Text(
                                "Loading installed apps...",
                                style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted),
                                modifier = Modifier.padding(16.dp),
                            )
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(24.dp)) }

            item {
                Text(
                    text = "Touch Grass v1.0.0 — Go outside.",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                )
            }
        }
    }
}

@Composable
private fun AppSectionLabel(label: String, color: androidx.compose.ui.graphics.Color) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium.copy(
            color = color,
            fontWeight = FontWeight.SemiBold,
        ),
        modifier = Modifier.padding(start = 20.dp, top = 12.dp, bottom = 4.dp),
    )
}

@Composable
private fun TimePickerRow(
    label: String,
    emoji: String,
    hour: Int,
    minute: Int,
    context: android.content.Context,
    onTimePicked: (Int, Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(emoji, style = MaterialTheme.typography.bodyLarge)
            Text(label, style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary))
        }
        TextButton(onClick = {
            TimePickerDialog(context, { _, h, m -> onTimePicked(h, m) }, hour, minute, true).show()
        }) {
            Text(
                text = "%02d:%02d".format(hour, minute),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = GrassGreen, fontWeight = FontWeight.Bold,
                ),
            )
        }
    }
}

@Composable
private fun AppBlockRow(
    app: InstalledApp,
    isBlocked: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (isBlocked) AccentRed.copy(alpha = 0.15f) else DarkCard,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = app.appName.firstOrNull()?.uppercase() ?: "?",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = if (isBlocked) AccentRed else TextSecondary,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = app.appName,
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary),
                    maxLines = 1,
                )
                Text(
                    text = app.packageName,
                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted),
                    maxLines = 1,
                )
            }
        }
        Switch(
            checked = isBlocked,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = AccentRed,
                checkedTrackColor = AccentRed.copy(alpha = 0.3f),
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = DarkDivider,
            ),
        )
    }
}

@Composable
private fun NameEditDialog(
    currentName: String,
    onValueChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = DarkCard,
        title = {
            Text(
                "What's your name?",
                style = MaterialTheme.typography.titleMedium.copy(color = TextPrimary),
            )
        },
        text = {
            OutlinedTextField(
                value = currentName,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Enter your name", style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted))
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GrassGreen,
                    unfocusedBorderColor = DarkDivider,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    cursorColor = GrassGreen,
                ),
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = currentName.isNotBlank(),
            ) {
                Text("Save", style = MaterialTheme.typography.labelLarge.copy(color = GrassGreen))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", style = MaterialTheme.typography.labelLarge.copy(color = TextMuted))
            }
        },
    )
}

@Composable
private fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium.copy(
                color = TextMuted,
                fontWeight = FontWeight.SemiBold,
            ),
            modifier = Modifier.padding(bottom = 8.dp),
        )
        GlassmorphicCard(content = content)
    }
}

@Composable
private fun ToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary))
            Text(subtitle, style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
        }
        Switch(
            checked = checked,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = GrassGreen,
                checkedTrackColor = GrassGreen.copy(alpha = 0.3f),
            ),
        )
    }
}
