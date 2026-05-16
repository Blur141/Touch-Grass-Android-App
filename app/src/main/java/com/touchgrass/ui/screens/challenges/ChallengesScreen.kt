package com.touchgrass.ui.screens.challenges

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.touchgrass.domain.model.Challenge
import com.touchgrass.ui.components.ChallengeCard
import com.touchgrass.ui.components.GlassmorphicCard
import com.touchgrass.ui.theme.*

@Composable
fun ChallengesScreen(viewModel: ChallengesViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val justCompleted by viewModel.justCompleted.collectAsStateWithLifecycle()

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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Text(
                        "Daily Challenges 🌿",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                    )
                    Text(
                        "Touch grass. Earn XP. Be human.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextMuted),
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    ChallengeStatChip(
                        emoji = "✅",
                        value = "${uiState.todaysChallenges.count { it.isCompleted }}/${uiState.todaysChallenges.size}",
                        label = "Today",
                        modifier = Modifier.weight(1f),
                    )
                    ChallengeStatChip(
                        emoji = "⭐",
                        value = "${uiState.totalXpEarned} XP",
                        label = "Earned",
                        modifier = Modifier.weight(1f),
                    )
                    ChallengeStatChip(
                        emoji = "🏆",
                        value = "${uiState.completedCount}",
                        label = "All Time",
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            item { Spacer(Modifier.height(20.dp)) }

            item {
                Text(
                    "Today's Missions",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 20.dp),
                )
            }

            item { Spacer(Modifier.height(8.dp)) }

            items(uiState.todaysChallenges) { challenge ->
                ChallengeCard(
                    challenge = challenge,
                    onComplete = viewModel::completeChallenge,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                )
            }

            if (uiState.completedChallenges.isNotEmpty()) {
                item { Spacer(Modifier.height(20.dp)) }
                item {
                    Text(
                        "Recent Completions",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 20.dp),
                    )
                }
                item { Spacer(Modifier.height(8.dp)) }
                items(uiState.completedChallenges) { challenge ->
                    ChallengeCard(
                        challenge = challenge,
                        onComplete = {},
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                    )
                }
            }
        }

        justCompleted?.let { challenge ->
            ChallengeCompletedDialog(challenge = challenge, onDismiss = viewModel::dismissJustCompleted)
        }
    }
}

@Composable
private fun ChallengeStatChip(
    emoji: String,
    value: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    GlassmorphicCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = emoji, fontSize = 20.sp)
            Spacer(Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold, color = GrassGreen))
            Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
        }
    }
}

@Composable
private fun ChallengeCompletedDialog(challenge: Challenge, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(32.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = challenge.emoji, fontSize = 64.sp)
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Challenge Complete!",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold, color = GrassGreen),
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = challenge.title,
                    style = MaterialTheme.typography.titleMedium.copy(color = TextPrimary),
                )
                Spacer(Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(AccentYellow.copy(alpha = 0.15f))
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "+${challenge.xpReward} XP",
                        style = MaterialTheme.typography.titleLarge.copy(color = AccentYellow, fontWeight = FontWeight.Black),
                    )
                }
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = GrassGreen),
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Text("Keep Going 💪", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
