package com.example.giga17.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.giga17.data.model.Siswa
import com.example.giga17.data.repository.LeaderboardRepository
import com.example.giga17.data.repository.SiswaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

sealed class LeaderboardState {
    object Loading : LeaderboardState()
    data class Success(
        val topUsers: List<Siswa>,
        val currentUserRank: Int?,
        val currentSiswa: Siswa?
    ) : LeaderboardState()
    data class Error(val message: String) : LeaderboardState()
}

class LeaderboardViewModel(
    private val leaderboardRepository: LeaderboardRepository,
    private val siswaRepository: SiswaRepository
) : ViewModel() {

    val leaderboardState: StateFlow<LeaderboardState> = combine(
        leaderboardRepository.getRealTimeLeaderboard(),
        siswaRepository.observeCurrentSiswa()
    ) { topUsers, currentSiswa ->
        val currentRankIndex = topUsers.indexOfFirst { it.nim == currentSiswa?.nim }
        val currentRank = if (currentRankIndex != -1) currentRankIndex + 1 else null
        
        LeaderboardState.Success(
            topUsers = topUsers,
            currentUserRank = currentRank,
            currentSiswa = currentSiswa
        ) as LeaderboardState
    }
    .catch { e ->
        emit(LeaderboardState.Error("Gagal memuat leaderboard: ${e.localizedMessage}"))
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LeaderboardState.Loading
    )

    companion object {
        fun provideFactory(
            leaderboardRepository: LeaderboardRepository,
            siswaRepository: SiswaRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(LeaderboardViewModel::class.java)) {
                    return LeaderboardViewModel(leaderboardRepository, siswaRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
