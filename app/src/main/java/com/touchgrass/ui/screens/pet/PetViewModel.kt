package com.touchgrass.ui.screens.pet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.touchgrass.data.repository.GamificationRepository
import com.touchgrass.domain.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class PetUiState(
    val profile: UserProfile = UserProfile(),
    val petName: String = "Grasshopper",
    val moodMessage: String = "Doing great!",
    val tips: List<String> = emptyList(),
)

@HiltViewModel
class PetViewModel @Inject constructor(
    private val gamificationRepository: GamificationRepository,
) : ViewModel() {

    val uiState: StateFlow<PetUiState> = gamificationRepository.observeProfile()
        .map { profile ->
            PetUiState(
                profile = profile,
                petName = getPetName(profile.petLevel),
                moodMessage = getMoodMessage(profile.petHealthPoints),
                tips = getTips(profile.petHealthPoints),
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PetUiState())

    private fun getPetName(level: Int) = when (level) {
        1 -> "Baby Sprout 🌱"
        2 -> "Little Herb 🪴"
        3 -> "Leafy Buddy 🌿"
        4 -> "Tree Pal 🌲"
        else -> "Ancient Oak 🌳"
    }

    private fun getMoodMessage(health: Int) = when {
        health >= 80 -> "Thriving! You've been offline a lot. Legend. 🌟"
        health >= 60 -> "Doing pretty good! Keep it up. 🌿"
        health >= 40 -> "A bit stressed. Put the phone down. 😟"
        health >= 20 -> "Wilting hard. Your doomscrolling is hurting me. 😢"
        else -> "I'm basically dead. Please. Go. Outside. 💀"
    }

    private fun getTips(health: Int) = when {
        health >= 70 -> listOf(
            "Complete a challenge for bonus health",
            "Your plant loves it when you focus",
            "Keep your streak going!",
        )
        else -> listOf(
            "Put your phone down for 30 minutes",
            "Go outside and breathe some air",
            "Complete today's challenges",
            "Start a focus session",
        )
    }
}
