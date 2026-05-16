package com.touchgrass.ui.screens.blocking

import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.touchgrass.ui.components.AnimatedProgressRing
import com.touchgrass.ui.theme.*

@Composable
fun BlockingScreen(
    packageName: String,
    onGoHome: () -> Unit,
    onBypassGranted: () -> Unit,
    viewModel: BlockingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val appName = remember(packageName) {
        runCatching {
            context.packageManager.getApplicationLabel(
                context.packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            ).toString()
        }.getOrDefault(packageName.substringAfterLast('.'))
    }

    // Minimize to Android home screen so the blocked app cannot be resumed via back
    val minimizeAndGoHome: () -> Unit = remember {
        {
            val homeIntent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(homeIntent)
            onGoHome() // Clean up nav state so Touch Grass shows Home when returned to
        }
    }

    // Back button → Android home, NOT back to blocked app
    BackHandler { minimizeAndGoHome() }

    LaunchedEffect(Unit) { viewModel.startCooldown() }

    LaunchedEffect(uiState.bypassUsed) {
        if (uiState.bypassUsed) onBypassGranted()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0D0010), DarkBackground),
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(64.dp))

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(AccentRed.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.Default.Block,
                    contentDescription = null,
                    tint = AccentRed,
                    modifier = Modifier.size(40.dp),
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "BLOCKED",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = AccentRed,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp,
                ),
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = appName,
                style = MaterialTheme.typography.displaySmall.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                ),
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "You added this app to your block list.\nGo touch some grass instead.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                ),
            )

            Spacer(Modifier.height(40.dp))

            AnimatedProgressRing(
                progress = if (uiState.isBypassReady) 1f else uiState.cooldownSeconds / 30f,
                size = 140.dp,
                strokeWidth = 10.dp,
                progressColor = if (uiState.isBypassReady) AccentRed else AccentPurple,
                trackColor = DarkDivider,
                centerContent = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (uiState.isBypassReady) {
                            Text(
                                text = "!",
                                style = MaterialTheme.typography.displaySmall.copy(
                                    color = AccentRed,
                                    fontWeight = FontWeight.Black,
                                ),
                            )
                        } else {
                            Text(
                                text = "${uiState.cooldownSeconds}",
                                style = MaterialTheme.typography.displaySmall.copy(
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Black,
                                ),
                            )
                            Text(
                                text = "sec",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted),
                            )
                        }
                    }
                },
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = if (uiState.isBypassReady) "Bypass available" else "Wait before you can bypass",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = if (uiState.isBypassReady) AccentRed else TextMuted,
                ),
            )

            Spacer(Modifier.height(40.dp))

            // Go HOME — sends user to Android home screen, not just Touch Grass
            Button(
                onClick = minimizeAndGoHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GrassGreen),
            ) {
                Text(
                    "Take a Break 🌿",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    ),
                )
            }

            Spacer(Modifier.height(12.dp))

            AnimatedVisibility(
                visible = uiState.isBypassReady,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut(),
            ) {
                OutlinedButton(
                    onClick = { viewModel.useEmergencyBypass(packageName) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, AccentRed.copy(alpha = 0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentRed),
                ) {
                    Text(
                        "Emergency Bypass (-${uiState.xpPenalty} XP) ⚠️",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Bypass costs ${uiState.xpPenalty} XP and grants 5 minutes of access.",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                ),
            )
        }
    }
}
