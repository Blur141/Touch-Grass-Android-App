package com.touchgrass.ui.screens.onboarding

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.touchgrass.ui.theme.*
import kotlinx.coroutines.launch

private data class OnboardingPage(
    val emoji: String,
    val title: String,
    val subtitle: String,
    val accentColor: Color,
)

private val onboardingPages = listOf(
    OnboardingPage("🌱", "Touch Grass", "Your digital detox companion that makes going offline actually fun.", GrassGreen),
    OnboardingPage("📊", "Track Screen Time", "See exactly how many hours you've given to the algorithm. It's a lot.", AccentBlue),
    OnboardingPage("🌿", "Daily Challenges", "Touch actual grass. Walk outside. Drink water. Earn XP for being human.", GrassGreen),
    OnboardingPage("⚡", "Focus Mode", "Lock in for 25 minutes. No Instagram. No excuses. Just you and work.", AccentPurple),
    OnboardingPage("🪴", "Grow Your Plant", "Your virtual plant lives or dies based on how much you use your phone. Make it thrive.", GrassGreen),
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()
    val currentPage = pagerState.currentPage
    val accentColor = onboardingPages[currentPage].accentColor

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Subtle background glow that changes per page
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.radialGradient(
                        colors = listOf(accentColor.copy(alpha = 0.08f), Color.Transparent),
                        radius = 600f,
                    )
                )
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            val pageData = onboardingPages[page]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(text = pageData.emoji, fontSize = 88.sp)
                Spacer(Modifier.height(36.dp))
                Text(
                    text = pageData.title,
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Black,
                        color = TextPrimary,
                        textAlign = TextAlign.Center,
                    ),
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = pageData.subtitle,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp,
                    ),
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 28.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Page indicator dots
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(onboardingPages.size) { index ->
                    val isSelected = index == currentPage
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 28.dp else 8.dp, 8.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) accentColor else DarkDivider)
                            .animateContentSize()
                    )
                }
            }
            Spacer(Modifier.height(28.dp))
            Button(
                onClick = {
                    if (currentPage < onboardingPages.size - 1) {
                        scope.launch { pagerState.animateScrollToPage(currentPage + 1) }
                    } else {
                        viewModel.completeOnboarding(onComplete)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
            ) {
                Text(
                    text = if (currentPage < onboardingPages.size - 1) "Continue" else "Let's Go 🌿",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White, fontWeight = FontWeight.Bold,
                    ),
                )
            }
            if (currentPage < onboardingPages.size - 1) {
                Spacer(Modifier.height(12.dp))
                TextButton(onClick = { viewModel.completeOnboarding(onComplete) }) {
                    Text("Skip", style = MaterialTheme.typography.bodyMedium.copy(color = TextMuted))
                }
            }
        }
    }
}
