package com.touchgrass.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.touchgrass.domain.model.Challenge
import com.touchgrass.domain.model.ChallengeDifficulty
import com.touchgrass.ui.theme.*

@Composable
fun ChallengeCard(
    challenge: Challenge,
    onComplete: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val difficultyColor = Color(challenge.difficulty.color)
    var showFunFact by remember { mutableStateOf(false) }

    GlassmorphicCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { showFunFact = !showFunFact },
        borderColor = if (challenge.isCompleted) GrassGreen.copy(alpha = 0.6f) else DarkDivider,
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(DarkCardElevated),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = challenge.emoji, fontSize = 26.sp)
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = challenge.title,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary),
                    )
                    DifficultyChip(challenge.difficulty)
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = challenge.description,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextMuted),
                    maxLines = 2,
                )
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    XpChip(challenge.xpReward)
                    if (challenge.durationMinutes > 0) {
                        TimeChip(challenge.durationMinutes)
                    }
                }
            }
            Spacer(Modifier.width(12.dp))
            AnimatedContent(
                targetState = challenge.isCompleted,
                transitionSpec = { scaleIn(tween(300)) togetherWith scaleOut(tween(300)) },
                label = "check_anim",
            ) { completed ->
                if (completed) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(GrassGreen),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Completed", tint = DarkBackground)
                    }
                } else {
                    OutlinedButton(
                        onClick = { onComplete(challenge.id) },
                        modifier = Modifier.height(36.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GrassGreen),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GrassGreen),
                        contentPadding = PaddingValues(horizontal = 12.dp),
                    ) {
                        Text("Done", style = MaterialTheme.typography.labelMedium.copy(color = GrassGreen))
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = showFunFact && challenge.funFact.isNotEmpty(),
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkCardElevated)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "💡 ${challenge.funFact}",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                )
            }
        }
    }
}

@Composable
private fun DifficultyChip(difficulty: ChallengeDifficulty) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color(difficulty.color).copy(alpha = 0.15f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = difficulty.label,
            style = MaterialTheme.typography.labelSmall.copy(color = Color(difficulty.color), fontWeight = FontWeight.SemiBold),
        )
    }
}

@Composable
private fun XpChip(xp: Int) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(AccentYellow.copy(alpha = 0.15f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "+$xp XP",
            style = MaterialTheme.typography.labelSmall.copy(color = AccentYellow, fontWeight = FontWeight.Bold),
        )
    }
}

@Composable
private fun TimeChip(minutes: Int) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(AccentBlue.copy(alpha = 0.15f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = "${minutes}m",
            style = MaterialTheme.typography.labelSmall.copy(color = AccentBlue),
        )
    }
}
