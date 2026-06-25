package com.example.giga17.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.giga17.di.AppContainer
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.giga17.presentation.ui.auth.LoginScreen
import com.example.giga17.presentation.viewmodel.AuthViewModel

@Composable
fun Giga17NavGraph(
    appContainer: AppContainer,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModel.provideFactory(appContainer.siswaRepository)
            )
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Dashboard.route) {
            val dashboardViewModel: com.example.giga17.presentation.viewmodel.DashboardViewModel = viewModel(
                factory = com.example.giga17.presentation.viewmodel.DashboardViewModel.provideFactory(
                    appContainer.siswaRepository,
                    appContainer.progresRepository,
                    appContainer.babRepository
                )
            )
            val gamificationViewModel: com.example.giga17.presentation.viewmodel.GamificationViewModel = viewModel(
                factory = com.example.giga17.presentation.viewmodel.GamificationViewModel.provideFactory(
                    appContainer.siswaRepository,
                    appContainer.progresRepository,
                    appContainer.babRepository
                )
            )
            com.example.giga17.presentation.ui.dashboard.DashboardScreen(
                viewModel = dashboardViewModel,
                gamificationViewModel = gamificationViewModel,
                onNavigateToTerminal = {
                    navController.navigate(Screen.TerminalPascal.route)
                },
                onNavigateToMateri = { subBabId ->
                    navController.navigate(Screen.SubBabDetail.createRoute(subBabId))
                },
                onNavigateToLeaderboard = {
                    navController.navigate(Screen.Leaderboard.route)
                }
            )
        }

        composable(Screen.DaftarMateri.route) {
            val dashboardViewModel: com.example.giga17.presentation.viewmodel.DashboardViewModel = viewModel(
                viewModelStoreOwner = androidx.compose.ui.platform.LocalContext.current as androidx.activity.ComponentActivity,
                factory = com.example.giga17.presentation.viewmodel.DashboardViewModel.provideFactory(
                    appContainer.siswaRepository,
                    appContainer.progresRepository,
                    appContainer.babRepository
                )
            )
            com.example.giga17.presentation.ui.materi.DaftarMateriScreen(
                viewModel = dashboardViewModel,
                onNavigateToDetail = { subBabId ->
                    navController.navigate(Screen.SubBabDetail.createRoute(subBabId))
                },
                onNavigateToBabQuiz = { babId ->
                    navController.navigate(Screen.Kuis.createRoute("BAB", babId))
                },
                onNavigateToSimulasi = { babId ->
                    navController.navigate(Screen.Simulasi.createRoute(babId))
                }
            )
        }

        composable(Screen.Leaderboard.route) {
            val leaderboardViewModel: com.example.giga17.presentation.viewmodel.LeaderboardViewModel = viewModel(
                factory = com.example.giga17.presentation.viewmodel.LeaderboardViewModel.provideFactory(
                    appContainer.leaderboardRepository,
                    appContainer.siswaRepository
                )
            )
            com.example.giga17.presentation.ui.leaderboard.LeaderboardScreen(
                viewModel = leaderboardViewModel
            )
        }

        composable(Screen.SubBabDetail.route) { backStackEntry ->
            val subBabId = backStackEntry.arguments?.getString("subBabId")?.toIntOrNull() ?: 1
            
            val kuisViewModel: com.example.giga17.presentation.viewmodel.KuisViewModel = viewModel(
                viewModelStoreOwner = androidx.compose.ui.platform.LocalContext.current as androidx.activity.ComponentActivity,
                factory = com.example.giga17.presentation.viewmodel.KuisViewModel.provideFactory(
                    appContainer.babRepository,
                    appContainer.progresRepository,
                    appContainer.siswaRepository
                )
            )
            
            com.example.giga17.presentation.ui.materi.DetailSubBabScreen(
                subBabId = subBabId,
                viewModel = kuisViewModel,
                onNavigateToKuis = { targetId ->
                    navController.navigate(Screen.Kuis.createRoute("SUBBAB", targetId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Kuis.route) { backStackEntry ->
            val targetType = backStackEntry.arguments?.getString("targetType") ?: "SUBBAB"
            val targetId = backStackEntry.arguments?.getString("targetId")?.toIntOrNull() ?: 1
            
            val kuisViewModel: com.example.giga17.presentation.viewmodel.KuisViewModel = viewModel(
                viewModelStoreOwner = androidx.compose.ui.platform.LocalContext.current as androidx.activity.ComponentActivity,
                factory = com.example.giga17.presentation.viewmodel.KuisViewModel.provideFactory(
                    appContainer.babRepository,
                    appContainer.progresRepository,
                    appContainer.siswaRepository
                )
            )
            
            com.example.giga17.presentation.ui.kuis.KuisScreen(
                targetType = targetType,
                targetId = targetId,
                viewModel = kuisViewModel,
                onBack = { 
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Profil.route) {
            val profilViewModel: com.example.giga17.presentation.viewmodel.ProfilViewModel = viewModel(
                factory = com.example.giga17.presentation.viewmodel.ProfilViewModel.provideFactory(
                    appContainer.siswaRepository,
                    appContainer.pencapaianRepository
                )
            )
            com.example.giga17.presentation.ui.profil.ProfilScreen(
                viewModel = profilViewModel,
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Simulasi.route) { backStackEntry ->
            val babId = backStackEntry.arguments?.getString("babId")?.toIntOrNull() ?: 1
            val coroutineScope = rememberCoroutineScope()
            com.example.giga17.presentation.ui.materi.SimulasiScreen(
                babId = babId,
                onBack = { navController.popBackStack() },
                onSimulasiTried = {
                    coroutineScope.launch {
                        val siswaId = appContainer.siswaRepository.currentSiswaId
                        if (siswaId != null) {
                            appContainer.observationRepository.logEvent(
                                siswaId = siswaId,
                                eventType = "SIMULASI_TRIED_$babId",
                                eventDetail = "Simulasi Bab $babId dicoba"
                            )
                            appContainer.pencapaianRepository.checkAndUnlockBadges(siswaId)
                        }
                    }
                }
            )
        }

        composable(Screen.TerminalPascal.route) {
            val terminalViewModel: com.example.giga17.presentation.viewmodel.TerminalViewModel = viewModel(
                factory = com.example.giga17.presentation.viewmodel.TerminalViewModel.provideFactory(
                    appContainer.compilerRepository,
                    appContainer.siswaRepository
                )
            )
            com.example.giga17.presentation.ui.terminal.TerminalPascalScreen(
                viewModel = terminalViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
