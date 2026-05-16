package com.touchgrass.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import android.content.Intent
import android.provider.Settings
import com.touchgrass.ui.components.*
import com.touchgrass.ui.theme.*

@Composable
fun HomeScreen(
    onNavigateToStats: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = androidx.compose.ui.platform.LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    // Re-check permission when coming back from Settings
    DisposableEffect(lifecycle) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) viewModel.recheckPermissions()
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = GrassGreen,
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp),
            ) {
                item { HomeTopBar(uiState, onNavigateToSettings) }
                if (!uiState.hasUsageStatsPermission) {
                    item {
                        UsagePermissionBanner(onClick = {
                            context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                        })
                    }
                }
                if (!uiState.hasOverlayPermission) {
                    item {
                        OverlayPermissionBanner(onClick = {
                            context.startActivity(
                                Intent(
                                    android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    android.net.Uri.parse("package:${context.packageName}"),
                                )
                            )
                        })
                    }
                }
                item { Spacer(Modifier.height(8.dp)) }
                item { ScreenTimeHeroCard(uiState, viewModel) }
                item { Spacer(Modifier.height(16.dp)) }
                item { QuickStatsRow(uiState) }
                item { Spacer(Modifier.height(20.dp)) }
                item {
                    SectionHeader(
                        title = "Today's Challenges",
                        subtitle = "${uiState.todaysChallenges.count { it.isCompleted }}/${uiState.todaysChallenges.size} done",
                        emoji = "🌿",
                    )
                }
                item { Spacer(Modifier.height(8.dp)) }
                items(uiState.todaysChallenges.take(3)) { challenge ->
                    ChallengeCard(
                        challenge = challenge,
                        onComplete = viewModel::completeChallenge,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                    )
                }
                item { Spacer(Modifier.height(20.dp)) }
                if (uiState.topApps.isNotEmpty()) {
                    item {
                        SectionHeader(title = "Top Apps Today", emoji = "📱")
                    }
                    item { Spacer(Modifier.height(8.dp)) }
                    item { TopAppsRow(uiState.topApps) }
                }
                item { Spacer(Modifier.height(20.dp)) }
                item { TouchGrassScoreCard(uiState) }
            }
        }
    }
}

@Composable
private fun HomeTopBar(uiState: HomeUiState, onSettings: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = "Hey, ${uiState.profile.name} 👋",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
            )
            Text(
                text = uiState.profile.levelTitle,
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            StreakBadge(streak = uiState.profile.streak)
            IconButton(
                onClick = onSettings,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(DarkCard),
            ) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = TextSecondary)
            }
        }
    }
}

@Composable
private fun ScreenTimeHeroCard(uiState: HomeUiState, viewModel: HomeViewModel) {
    GlassmorphicCard(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Screen Time",
                    style = MaterialTheme.typography.titleMedium.copy(color = TextMuted),
                )
                IconButton(onClick = viewModel::refreshUsageStats) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = TextMuted, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ScreenTimeProgressRing(
                    progress = uiState.dailySummary.goalProgress,
                    screenTimeText = uiState.dailySummary.totalScreenTimeFormatted,
                    goalText = "${uiState.dailySummary.dailyGoalMs / 3_600_000}h goal",
                    size = 150.dp,
                )
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    MiniStat("🔓", "Unlocks", "${uiState.dailySummary.unlockCount}x")
                    MiniStat("⏱️", "Longest", uiState.dailySummary.let {
                        val m = it.longestSessionMs / 60_000
                        "${m}m"
                    })
                    MiniStat(
                        if (uiState.dailySummary.isOverGoal) "⚠️" else "✅",
                        "Goal",
                        if (uiState.dailySummary.isOverGoal) "Over!" else "On track",
                        tint = if (uiState.dailySummary.isOverGoal) AccentRed else GrassGreen,
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            XpProgressBar(
                currentXp = uiState.profile.xp % (uiState.profile.level * 500),
                levelXp = uiState.profile.level * 500,
                level = uiState.profile.level,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun MiniStat(emoji: String, label: String, value: String, tint: Color = TextSecondary) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(text = emoji, fontSize = 14.sp)
        Column {
            Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
            Text(text = value, style = MaterialTheme.typography.labelMedium.copy(color = tint, fontWeight = FontWeight.SemiBold))
        }
    }
}

@Composable
private fun QuickStatsRow(uiState: HomeUiState) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            StatCard(
                label = "Challenges Done",
                value = "${uiState.profile.totalChallengesCompleted}",
                emoji = "🌿",
                modifier = Modifier.width(150.dp),
                accentColor = GrassGreen,
            )
        }
        item {
            StatCard(
                label = "Focus Time",
                value = "${uiState.profile.totalFocusMinutes}m",
                emoji = "⚡",
                modifier = Modifier.width(150.dp),
                accentColor = AccentBlue,
            )
        }
        item {
            StatCard(
                label = "Best Streak",
                value = "${uiState.profile.longestStreak}d",
                emoji = "🔥",
                modifier = Modifier.width(150.dp),
                accentColor = AccentOrange,
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String, emoji: String, subtitle: String? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = emoji, fontSize = 18.sp)
            Text(text = title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        }
        if (subtitle != null) {
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall.copy(color = TextMuted))
        }
    }
}

@Composable
private fun TopAppsRow(apps: List<com.touchgrass.domain.model.AppUsage>) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(apps) { app ->
            GlassmorphicCard(modifier = Modifier.width(120.dp)) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = when {
                            app.packageName.contains("instagram") -> "📸"
                            app.packageName.contains("youtube") -> "📺"
                            app.packageName.contains("tiktok") -> "🎵"
                            app.packageName.contains("twitter") -> "🐦"
                            app.packageName.contains("reddit") -> "👽"
                            else -> "📱"
                        },
                        fontSize = 28.sp,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = app.appName.take(10),
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary),
                        maxLines = 1,
                    )
                    Text(
                        text = app.totalTimeFormatted,
                        style = MaterialTheme.typography.labelMedium.copy(color = TextPrimary, fontWeight = FontWeight.Bold),
                    )
                }
            }
        }
    }
}

@Composable
private fun TouchGrassScoreCard(uiState: HomeUiState) {
    val score = uiState.dailySummary.touchGrassScore
    val scoreColor = when {
        score >= 70 -> GrassGreen
        score >= 40 -> AccentYellow
        else -> AccentRed
    }
    val scoreMessage = when {
        score >= 80 -> "Legendary. Go touch more grass. 🏆"
        score >= 60 -> "Solid. Outside is proud of you. 🌿"
        score >= 40 -> "Mid. You can do better. 📵"
        score >= 20 -> "Yikes. Put the phone down. 💀"
        else -> "We're concerned. Grass is outside. 🌱"
    }

    GradientCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        gradientColors = listOf(scoreColor.copy(alpha = 0.2f), DarkCard),
        cornerRadius = 20.dp,
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Touch Grass Score",
                style = MaterialTheme.typography.labelMedium.copy(color = TextMuted),
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "$score",
                    style = MaterialTheme.typography.displaySmall.copy(color = scoreColor, fontWeight = FontWeight.Black),
                )
                Text(
                    text = "/ 100",
                    style = MaterialTheme.typography.titleMedium.copy(color = TextMuted),
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = scoreMessage,
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
            )
        }
    }
}

@Composable
private fun UsagePermissionBanner(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(AccentOrange.copy(alpha = 0.12f))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("⚠️", fontSize = 20.sp)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "Enable Screen Time Access",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = AccentOrange,
                    fontWeight = FontWeight.SemiBold,
                ),
            )
            Text(
                "Tap to grant Usage Stats — needed for screen time tracking and app blocking.",
                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary),
            )
        }
        Text(
            "Grant →",
            style = MaterialTheme.typography.labelSmall.copy(
                color = AccentOrange,
                fontWeight = FontWeight.Bold,
            ),
        )
    }
}

@Composable
private fun OverlayPermissionBanner(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(AccentRed.copy(alpha = 0.10f))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("🚫", fontSize = 20.sp)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "Enable App Blocking",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = AccentRed,
                    fontWeight = FontWeight.SemiBold,
                ),
            )
            Text(
                "Tap to allow Touch Grass to display over other apps — required to block distracting apps.",
                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary),
            )
        }
        Text(
            "Grant →",
            style = MaterialTheme.typography.labelSmall.copy(
                color = AccentRed,
                fontWeight = FontWeight.Bold,
            ),
        )
    }
}
