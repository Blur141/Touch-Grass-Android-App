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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.touchgrass.ui.theme.*

@Composable
fun VirtualPetDisplay(
    healthPoints: Int,
    petLevel: Int,
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
) {
    val mood = when {
        healthPoints >= 80 -> PetMood.HAPPY
        healthPoints >= 50 -> PetMood.NEUTRAL
        healthPoints >= 20 -> PetMood.SAD
        else -> PetMood.DYING
    }

    val breatheAnim = rememberInfiniteTransition(label = "breathe")
    val scale by breatheAnim.animateFloat(
        initialValue = 1f,
        targetValue = if (mood == PetMood.HAPPY) 1.05f else 0.98f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "breathe_scale",
    )

    val wobble by breatheAnim.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "wobble",
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .scale(scale),
            contentAlignment = Alignment.Center,
        ) {
            // Pot
            Box(
                modifier = Modifier
                    .size(size * 0.5f)
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 12.dp, bottomEnd = 12.dp))
                    .background(Color(0xFF8B6914))
            )
            // Plant stem and leaves based on level
            PlantEmoji(petLevel, mood, size)
        }

        Spacer(Modifier.height(8.dp))

        // Health bar
        Box(
            modifier = Modifier
                .width(size)
                .height(6.dp)
                .clip(CircleShape)
                .background(DarkDivider)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(healthPoints / 100f)
                    .clip(CircleShape)
                    .background(
                        when {
                            healthPoints >= 70 -> GrassGreen
                            healthPoints >= 40 -> AccentYellow
                            else -> AccentRed
                        }
                    )
            )
        }

        Spacer(Modifier.height(4.dp))

        Text(
            text = mood.message,
            style = MaterialTheme.typography.bodySmall.copy(color = TextMuted, textAlign = TextAlign.Center),
        )
    }
}

@Composable
private fun PlantEmoji(level: Int, mood: PetMood, size: Dp) {
    val emoji = when {
        mood == PetMood.DYING -> "🥀"
        mood == PetMood.SAD -> "🌱"
        level >= 5 -> "🌳"
        level >= 4 -> "🌲"
        level >= 3 -> "🌿"
        level >= 2 -> "🪴"
        else -> "🌱"
    }
    Text(
        text = emoji,
        fontSize = (size.value * 0.45f).sp,
        modifier = Modifier.offset(y = (-size.value * 0.1f).dp),
    )
}

private enum class PetMood(val message: String) {
    HAPPY("Thriving! 🌟"),
    NEUTRAL("Doing okay"),
    SAD("Needs sunshine 😢"),
    DYING("Put the phone down! 💀"),
}
