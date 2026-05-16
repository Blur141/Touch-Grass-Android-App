package com.touchgrass.ui.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Stats : Screen("stats")
    object Challenges : Screen("challenges")
    object Focus : Screen("focus")
    object Pet : Screen("pet")
    object Settings : Screen("settings")
    object Achievements : Screen("achievements")
    object Blocking : Screen("blocking/{packageName}") {
        fun createRoute(packageName: String) = "blocking/$packageName"
    }
}
