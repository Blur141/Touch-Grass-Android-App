package com.touchgrass.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.touchgrass.ui.screens.blocking.BlockingScreen
import com.touchgrass.ui.screens.challenges.ChallengesScreen
import com.touchgrass.ui.screens.focus.FocusScreen
import com.touchgrass.ui.screens.home.HomeScreen
import com.touchgrass.ui.screens.onboarding.OnboardingScreen
import com.touchgrass.ui.screens.pet.PetScreen
import com.touchgrass.ui.screens.settings.SettingsScreen
import com.touchgrass.ui.screens.stats.StatsScreen

@Composable
fun TouchGrassNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            slideInHorizontally(tween(300)) { it / 2 } + fadeIn(tween(300))
        },
        exitTransition = {
            slideOutHorizontally(tween(300)) { -it / 2 } + fadeOut(tween(300))
        },
        popEnterTransition = {
            slideInHorizontally(tween(300)) { -it / 2 } + fadeIn(tween(300))
        },
        popExitTransition = {
            slideOutHorizontally(tween(300)) { it / 2 } + fadeOut(tween(300))
        },
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(onComplete = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                }
            })
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToStats = { navController.navigate(Screen.Stats.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
            )
        }
        composable(Screen.Stats.route) {
            StatsScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Challenges.route) {
            ChallengesScreen()
        }
        composable(Screen.Focus.route) {
            FocusScreen()
        }
        composable(Screen.Pet.route) {
            PetScreen()
        }
        composable(Screen.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
        composable(
            route = Screen.Blocking.route,
            arguments = listOf(navArgument("packageName") { type = NavType.StringType }),
        ) { backStackEntry ->
            val packageName = backStackEntry.arguments?.getString("packageName") ?: ""
            BlockingScreen(
                packageName = packageName,
                onGoHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onBypassGranted = {
                    navController.popBackStack()
                },
            )
        }
    }
}
