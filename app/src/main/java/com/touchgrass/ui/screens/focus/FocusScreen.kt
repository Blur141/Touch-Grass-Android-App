package com.touchgrass.ui.screens.focus

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.touchgrass.domain.model.FocusSessionType
import com.touchgrass.ui.components.AnimatedProgressRing
import com.touchgrass.ui.theme.*

@Composable
fun FocusScreen(viewModel: FocusViewModel = hiltViewModel()) {
    val focusState by viewModel.focusState.collectAsStateWithLifecycle()
    val selectedType by viewModel.selectedType.collectAsStateWithLifecycle()
    val customMinutes by viewModel.customMinutes.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(20.dp))

            Text(
                text = if (focusState.isActive) "Focus Mode ⚡" else "Focus Mode",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
            )
            Text(
                text = if (focusState.isActive) selectedType.description else "Lock in. No excuses.",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted),
            )

            Spacer(Modifier.height(40.dp))

            FocusTimerRing(
                progress = focusState.progress,
                timeText = focusState.formattedTime,
                isActive = focusState.isActive,
                sessionEmoji = selectedType.emoji,
            )

            Spacer(Modifier.height(40.dp))

            AnimatedVisibility(
                visible = !focusState.isActive,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically(),
            ) {
                SessionTypeSelector(
                    selectedType = selectedType,
                    onSelect = viewModel::selectSessionType,
                    customMinutes = customMinutes,
                    onCustomMinutesChange = viewModel::setCustomMinutes,
                )
            }

            Spacer(Modifier.height(32.dp))

            FocusActionButton(
                isActive = focusState.isActive,
                onStart = viewModel::startSession,
                onStop = viewModel::stopSession,
            )

            AnimatedVisibility(visible = focusState.isActive) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = "Stay strong. You're doing great. 💪",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextMuted),
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "+${selectedType.defaultMinutes * 2} XP on completion",
                        style = MaterialTheme.typography.labelMedium.copy(color = AccentYellow),
                    )
                }
            }
        }
    }
}

@Composable
private fun FocusTimerRing(
    progress: Float,
    timeText: String,
    isActive: Boolean,
    sessionEmoji: String,
) {
    val pulseAnim = rememberInfiniteTransition(label = "pulse")
    val scale by pulseAnim.animateFloat(
        initialValue = 1f,
        targetValue = if (isActive) 1.02f else 1f,
        animationSpec = infiniteRepeatable(tween(1500), RepeatMode.Reverse),
        label = "ring_pulse",
    )

    AnimatedProgressRing(
        progress = progress,
        modifier = Modifier.scale(scale),
        size = 220.dp,
        strokeWidth = 16.dp,
        progressColor = if (isActive) AccentBlue else GrassGreen,
        trackColor = DarkDivider,
        centerContent = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = sessionEmoji, fontSize = 32.sp)
                Spacer(Modifier.height(4.dp))
                Text(
                    text = timeText,
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        fontSize = 42.sp,
                    ),
                )
                if (isActive) {
                    Text(
                        text = "In progress",
                        style = MaterialTheme.typography.labelSmall.copy(color = AccentBlue),
                    )
                }
            }
        }
    )
}

@Composable
private fun SessionTypeSelector(
    selectedType: FocusSessionType,
    onSelect: (FocusSessionType) -> Unit,
    customMinutes: Int,
    onCustomMinutesChange: (Int) -> Unit,
) {
    Column {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(FocusSessionType.values()) { type ->
                val isSelected = type == selectedType
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) GrassGreen.copy(alpha = 0.2f) else DarkCard)
                        .border(1.dp, if (isSelected) GrassGreen else DarkDivider, RoundedCornerShape(14.dp))
                        .clickable { onSelect(type) }
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = type.emoji, fontSize = 20.sp)
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = type.label,
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = if (isSelected) GrassGreen else TextMuted,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            ),
                        )
                        Text(
                            text = "${if (type == FocusSessionType.CUSTOM) customMinutes else type.defaultMinutes}m",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted),
                        )
                    }
                }
            }
        }

        AnimatedVisibility(visible = selectedType == FocusSessionType.CUSTOM) {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    text = "Custom duration: ${customMinutes}m",
                    style = MaterialTheme.typography.labelMedium.copy(color = TextSecondary),
                )
                Slider(
                    value = customMinutes.toFloat(),
                    onValueChange = { onCustomMinutesChange(it.toInt()) },
                    valueRange = 5f..180f,
                    steps = 34,
                    colors = SliderDefaults.colors(thumbColor = GrassGreen, activeTrackColor = GrassGreen),
                )
            }
        }
    }
}

@Composable
private fun FocusActionButton(
    isActive: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "btn_scale",
    )

    Button(
        onClick = if (isActive) onStop else onStart,
        modifier = Modifier
            .scale(scale)
            .height(56.dp)
            .widthIn(min = 180.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isActive) AccentRed else GrassGreen,
        ),
    ) {
        Icon(
            if (isActive) Icons.Default.Close else Icons.Default.PlayArrow,
            contentDescription = null,
            tint = Color.White,
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = if (isActive) "End Session" else "Start Focus",
            style = MaterialTheme.typography.titleMedium.copy(color = Color.White, fontWeight = FontWeight.Bold),
        )
    }
}

