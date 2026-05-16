package com.touchgrass.ui.screens.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.touchgrass.domain.model.Achievement
import com.touchgrass.ui.components.*
import com.touchgrass.ui.theme.*
import com.touchgrass.utils.TimeUtils

@Composable
fun StatsScreen(
    onBack: () -> Unit,
    viewModel: StatsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp),
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Your Stats 📊",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                    )
                }
            }

            item {
                GlassmorphicCard(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("This Week", style = MaterialTheme.typography.titleMedium.copy(color = TextMuted))
                        Spacer(Modifier.height(16.dp))
                        if (uiState.weeklyChartData.isNotEmpty()) {
                            val maxMs = uiState.weeklyData.maxOfOrNull { it.totalScreenTimeMs }?.toFloat() ?: 1f
                            WeeklyBarChart(
                                data = uiState.weeklyChartData,
                                maxValue = maxMs,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp),
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text("No data yet. Go use your phone a bit.", style = MaterialTheme.typography.bodySmall.copy(color = TextMuted))
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            WeekStat("Avg/day", TimeUtils.formatDuration(uiState.weeklyAvgMs))
                            WeekStat("Best day", TimeUtils.formatDuration(uiState.bestDayMs))
                            WeekStat("Total", "%.1fh".format(uiState.totalScreenHours))
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(20.dp)) }

            item {
                Text(
                    text = "🏆 Achievements",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }

            item { Spacer(Modifier.height(8.dp)) }

            items(uiState.allAchievements) { achievement ->
                AchievementRow(achievement)
            }

            item { Spacer(Modifier.height(20.dp)) }

            item {
                GlassmorphicCard(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Profile Stats", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Spacer(Modifier.height(16.dp))
                        ProfileStatRow("Level", "${uiState.profile.level} — ${uiState.profile.levelTitle}")
                        ProfileStatRow("Total XP", "${uiState.profile.xp} XP")
                        ProfileStatRow("Challenges", "${uiState.profile.totalChallengesCompleted} completed")
                        ProfileStatRow("Focus Time", "${uiState.profile.totalFocusMinutes} minutes")
                        ProfileStatRow("Best Streak", "${uiState.profile.longestStreak} days")
                    }
                }
            }
        }
    }
}

@Composable
private fun WeekStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GrassGreen))
        Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
    }
}

@Composable
private fun AchievementRow(achievement: Achievement) {
    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        borderColor = if (achievement.isUnlocked) AccentYellow.copy(alpha = 0.4f) else DarkDivider,
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (achievement.isUnlocked) AccentYellow.copy(alpha = 0.2f) else DarkCardElevated),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = achievement.emoji,
                    fontSize = 22.sp,
                    color = if (achievement.isUnlocked) Color.Unspecified else Color.Gray,
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = achievement.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = if (achievement.isUnlocked) TextPrimary else TextMuted,
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
                Text(
                    text = achievement.description,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted),
                )
                if (!achievement.isUnlocked && achievement.requirement > 1) {
                    Spacer(Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(3.dp)
                            .clip(CircleShape)
                            .background(DarkDivider)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(achievement.progress)
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(GrassGreen)
                        )
                    }
                }
            }
            if (achievement.isUnlocked) {
                Text(
                    text = "+${achievement.xpReward}",
                    style = MaterialTheme.typography.labelMedium.copy(color = AccentYellow, fontWeight = FontWeight.Bold),
                )
            }
        }
    }
}

@Composable
private fun ProfileStatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted))
        Text(text = value, style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold))
    }
}
