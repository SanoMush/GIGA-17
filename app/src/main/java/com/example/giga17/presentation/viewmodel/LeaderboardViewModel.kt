package com.example.giga17.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.giga17.data.model.Siswa
import com.example.giga17.data.repository.SiswaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
    private val siswaRepository: SiswaRepository
) : ViewModel() {

    private val _leaderboardState = MutableStateFlow<LeaderboardState>(LeaderboardState.Loading)
    val leaderboardState: StateFlow<LeaderboardState> = _leaderboardState.asStateFlow()

    init {
        loadLeaderboardData()
    }

    private fun loadLeaderboardData() {
        viewModelScope.launch {
            try {
                siswaRepository.getAllSiswaByXp().collectLatest { allSiswa ->
                    val currentSiswaId = siswaRepository.currentSiswaId
                    val currentSiswa = allSiswa.find { it.id == currentSiswaId }
                    val currentRankIndex = allSiswa.indexOfFirst { it.id == currentSiswaId }
                    val currentRank = if (currentRankIndex != -1) currentRankIndex + 1 else null
                    
                    val topUsers = allSiswa.take(20)
                    
                    _leaderboardState.value = LeaderboardState.Success(
                        topUsers = topUsers,
                        currentUserRank = currentRank,
                        currentSiswa = currentSiswa
                    )
                }
            } catch (e: Exception) {
                _leaderboardState.value = LeaderboardState.Error("Gagal memuat leaderboard: ${e.localizedMessage}")
            }
        }
    }

    companion object {
        fun provideFactory(
            siswaRepository: SiswaRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(LeaderboardViewModel::class.java)) {
                    return LeaderboardViewModel(siswaRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
