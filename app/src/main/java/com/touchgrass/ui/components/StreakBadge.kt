package com.touchgrass.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.touchgrass.ui.theme.*

@Composable
fun StreakBadge(
    streak: Int,
    modifier: Modifier = Modifier,
) {
    val pulseAnim = rememberInfiniteTransition(label = "streak_pulse")
    val scale by pulseAnim.animateFloat(
        initialValue = 1f,
        targetValue = if (streak > 0) 1.05f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulse",
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.linearGradient(
                    colors = if (streak > 0)
                        listOf(Color(0xFFFF6B35), Color(0xFFEF4444))
                    else
                        listOf(DarkCard, DarkCardElevated)
                )
            )
            .padding(horizontal = 14.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(text = if (streak > 0) "🔥" else "💤", fontSize = 16.sp)
                Text(
                    text = "$streak",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        fontSize = 18.sp,
                    ),
                )
            }
            Text(
                text = "streak",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 9.sp,
                ),
            )
        }
    }
}

@Composable
fun XpProgressBar(
    currentXp: Int,
    levelXp: Int,
    level: Int,
    modifier: Modifier = Modifier,
) {
    val progress by animateFloatAsState(
        targetValue = (currentXp.toFloat() / levelXp).coerceIn(0f, 1f),
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "xp_progress",
    )

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Level $level",
                style = MaterialTheme.typography.labelMedium.copy(color = AccentYellow, fontWeight = FontWeight.Bold),
            )
            Text(
                text = "$currentXp / $levelXp XP",
                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted),
            )
        }
        Spacer(Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape)
                .background(DarkDivider)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(listOf(AccentYellow, GrassGreen))
                    )
            )
        }
    }
}
