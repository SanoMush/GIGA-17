package com.example.giga17.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object DaftarMateri : Screen("daftar_materi")
    object Leaderboard : Screen("leaderboard")
    object SubBabDetail : Screen("sub_bab/{subBabId}") {
        fun createRoute(subBabId: Int) = "sub_bab/$subBabId"
    }
    object Kuis : Screen("kuis/{targetType}/{targetId}") {
        fun createRoute(targetType: String, targetId: Int) = "kuis/$targetType/$targetId"
    }
    object Simulasi : Screen("simulasi/{babId}") {
        fun createRoute(babId: Int) = "simulasi/$babId"
    }
    object Profil : Screen("profil")
    object TerminalPascal : Screen("terminal_pascal")
}
