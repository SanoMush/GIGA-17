package com.example.giga17.presentation.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.giga17.di.AppContainer
import com.example.giga17.presentation.navigation.Giga17NavGraph
import com.example.giga17.presentation.navigation.Screen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import com.example.giga17.domain.engine.GamificationEvent
import com.example.giga17.presentation.ui.components.AchievementUnlockedBanner
import com.example.giga17.presentation.ui.components.LevelUpDialog

@Composable
fun MainScreen(appContainer: AppContainer) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Tentukan route mana saja yang perlu menampilkan Bottom Navigation
    val showBottomBar = currentRoute in listOf(
        Screen.Dashboard.route,
        Screen.DaftarMateri.route,
        Screen.Leaderboard.route,
        Screen.Profil.route
    )

    // Event Queue for Gamification
    val gamificationEventQueue = remember { mutableStateListOf<GamificationEvent>() }

    LaunchedEffect(Unit) {
        appContainer.gamificationEventBus.events.collect { event ->
            gamificationEventQueue.add(event)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold { paddingValues ->
            Giga17NavGraph(
                appContainer = appContainer,
                modifier = Modifier.padding(paddingValues),
                navController = navController
            )
        }

        // Floating Bottom Navigation Bar
        if (showBottomBar) {
            Box(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                BottomNavigationBar(navController = navController)
            }
        }

        // Display current top event
        if (gamificationEventQueue.isNotEmpty()) {
            val currentEvent = gamificationEventQueue.first()
            
            when (currentEvent) {
                is GamificationEvent.LevelUp -> {
                    LevelUpDialog(
                        level = currentEvent.newLevel,
                        onDismiss = { gamificationEventQueue.removeAt(0) }
                    )
                }
                is GamificationEvent.BadgeUnlocked -> {
                    Box(modifier = Modifier.align(Alignment.TopCenter)) {
                        AchievementUnlockedBanner(
                            badge = currentEvent.badge,
                            onDismiss = { gamificationEventQueue.removeAt(0) }
                        )
                    }
                }
            }
        }
    }
}
