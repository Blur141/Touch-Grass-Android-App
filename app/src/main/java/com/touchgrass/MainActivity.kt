package com.touchgrass

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.touchgrass.data.datastore.UserPreferencesDataStore
import com.touchgrass.service.AppBlockingService
import com.touchgrass.ui.navigation.Screen
import com.touchgrass.ui.navigation.TouchGrassNavGraph
import com.touchgrass.ui.theme.*
import com.touchgrass.utils.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var preferencesDataStore: UserPreferencesDataStore
    @Inject lateinit var permissionUtils: PermissionUtils

    private val blockedPackageFlow = MutableStateFlow<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val onboardingCompleted = runBlocking {
            preferencesDataStore.userPreferences.first().onboardingCompleted
        }

        handleBlockingIntent(intent)
        startBlockingService()

        setContent {
            val prefs by preferencesDataStore.userPreferences.collectAsState(
                initial = com.touchgrass.data.datastore.UserPreferences()
            )
            TouchGrassTheme(darkTheme = prefs.isDarkMode) {
                TouchGrassApp(
                    startDestination = if (onboardingCompleted) Screen.Home.route else Screen.Onboarding.route,
                    blockedPackageFlow = blockedPackageFlow,
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleBlockingIntent(intent)
    }

    private fun startBlockingService() {
        val intent = Intent(this, AppBlockingService::class.java).apply {
            action = AppBlockingService.ACTION_START
        }
        startForegroundService(intent)
    }

    private fun handleBlockingIntent(intent: Intent?) {
        if (intent?.getBooleanExtra(AppBlockingService.EXTRA_SHOW_BLOCKING, false) == true) {
            val pkg = intent.getStringExtra(AppBlockingService.EXTRA_BLOCKED_PACKAGE)
            if (!pkg.isNullOrBlank()) {
                blockedPackageFlow.value = pkg
            }
        }
    }
}

private data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
)

private val bottomNavItems = listOf(
    BottomNavItem(Screen.Home.route, "Home", Icons.Default.Home),
    BottomNavItem(Screen.Challenges.route, "Challenges", Icons.Default.Star),
    BottomNavItem(Screen.Focus.route, "Focus", Icons.Default.Timer),
    BottomNavItem(Screen.Pet.route, "Plant", Icons.Default.Eco),
    BottomNavItem(Screen.Stats.route, "Stats", Icons.Default.BarChart),
)

@Composable
private fun TouchGrassApp(
    startDestination: String,
    blockedPackageFlow: MutableStateFlow<String?>,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val blockedPackage by blockedPackageFlow.collectAsStateWithLifecycle()

    val showBottomBar = currentRoute in bottomNavItems.map { it.route }

    LaunchedEffect(blockedPackage) {
        val pkg = blockedPackage ?: return@LaunchedEffect
        blockedPackageFlow.value = null
        navController.navigate(Screen.Blocking.createRoute(pkg)) {
            launchSingleTop = true
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp,
                    modifier = Modifier.navigationBarsPadding(),
                ) {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentRoute == item.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = GrassGreen,
                                selectedTextColor = GrassGreen,
                                indicatorColor = GrassGreen.copy(alpha = 0.15f),
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted,
                            ),
                        )
                    }
                }
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (showBottomBar) paddingValues.calculateBottomPadding() else 0.dp),
        ) {
            TouchGrassNavGraph(
                navController = navController,
                startDestination = startDestination,
            )
        }
    }
}
