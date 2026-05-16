package com.touchgrass.ui.screens.pet

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.touchgrass.ui.components.GlassmorphicCard
import com.touchgrass.ui.components.VirtualPetDisplay
import com.touchgrass.ui.theme.*

@Composable
fun PetScreen(viewModel: PetViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val health = uiState.profile.petHealthPoints
    val healthColor = when {
        health >= 70 -> GrassGreen
        health >= 40 -> AccentYellow
        else -> AccentRed
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(healthColor.copy(alpha = 0.05f), Color.Transparent)
                    )
                )
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 100.dp),
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = uiState.petName,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                    )
                    Text(
                        text = "Your digital plant friend",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextMuted),
                    )
                }
            }

            item {
                VirtualPetDisplay(
                    healthPoints = health,
                    petLevel = uiState.profile.petLevel,
                    modifier = Modifier.padding(vertical = 24.dp),
                    size = 160.dp,
                )
            }

            item {
                GlassmorphicCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    borderColor = healthColor.copy(alpha = 0.4f),
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "\"${uiState.moodMessage}\"",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary),
                        )
                        Spacer(Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            PetStat("Health", "$health%", healthColor)
                            PetStat("Level", "${uiState.profile.petLevel}", AccentPurple)
                            PetStat("Streak", "${uiState.profile.streak}d", AccentOrange)
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(20.dp)) }

            item {
                Text(
                    text = "How to keep me happy",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
            }

            item { Spacer(Modifier.height(8.dp)) }

            items(uiState.tips) { tip ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(GrassGreen)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = tip,
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                    )
                }
            }

            item { Spacer(Modifier.height(20.dp)) }

            item {
                GlassmorphicCard(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "How health works",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        )
                        Spacer(Modifier.height(8.dp))
                        HealthFactRow("📵", "Under goal screen time", "+15 health")
                        HealthFactRow("✅", "Completing challenges", "+10 health")
                        HealthFactRow("⚡", "Focus sessions", "+10 health")
                        HealthFactRow("📱", "Over goal screen time", "-10 health")
                        HealthFactRow("🔥", "2x over screen limit", "-20 health")
                    }
                }
            }
        }
    }
}

@Composable
private fun PetStat(label: String, value: String, color: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.titleMedium.copy(color = color, fontWeight = FontWeight.ExtraBold))
        Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
    }
}

@Composable
private fun HealthFactRow(emoji: String, action: String, effect: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = emoji, fontSize = 14.sp)
            Text(text = action, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
        }
        Text(
            text = effect,
            style = MaterialTheme.typography.labelMedium.copy(
                color = if (effect.startsWith("+")) GrassGreen else AccentRed,
                fontWeight = FontWeight.Bold,
            ),
        )
    }
}
