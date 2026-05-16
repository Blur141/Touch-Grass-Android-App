package com.touchgrass.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.touchgrass.ui.theme.AccentRed
import com.touchgrass.ui.theme.GrassGreen

@Composable
fun AnimatedProgressRing(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    strokeWidth: Dp = 12.dp,
    trackColor: Color = Color.Unspecified,
    progressColor: Color = GrassGreen,
    centerContent: @Composable BoxScope.() -> Unit = {},
) {
    val resolvedTrack = if (trackColor == Color.Unspecified)
        MaterialTheme.colorScheme.outlineVariant else trackColor

    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "progress_ring",
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = strokeWidth.toPx()
            val sweep = animatedProgress * 360f

            drawArc(
                color = resolvedTrack,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokePx, cap = StrokeCap.Round),
            )
            if (sweep > 0f) {
                drawArc(
                    color = progressColor,
                    startAngle = -90f,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = strokePx, cap = StrokeCap.Round),
                )
            }
        }
        centerContent()
    }
}

@Composable
fun ScreenTimeProgressRing(
    progress: Float,
    screenTimeText: String,
    goalText: String,
    modifier: Modifier = Modifier,
    size: Dp = 160.dp,
    progressColor: Color = GrassGreen,
) {
    val textColor = MaterialTheme.colorScheme.onSurface
    val subtextColor = MaterialTheme.colorScheme.onSurfaceVariant
    AnimatedProgressRing(
        progress = progress,
        modifier = modifier,
        size = size,
        strokeWidth = 14.dp,
        progressColor = if (progress > 1f) AccentRed else progressColor,
        centerContent = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = screenTimeText,
                    style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = textColor),
                )
                Text(
                    text = "of $goalText",
                    style = TextStyle(fontSize = 11.sp, color = subtextColor),
                )
            }
        }
    )
}
