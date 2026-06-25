package com.example.giga17.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.giga17.data.model.Bab
import com.example.giga17.data.model.Siswa
import com.example.giga17.data.model.SubBab
import com.example.giga17.data.repository.BabRepository
import com.example.giga17.data.repository.ProgresRepository
import com.example.giga17.data.repository.SiswaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

sealed class DashboardState {
    object Loading : DashboardState()
    data class Success(
        val siswa: Siswa,
        val babList: List<Bab>,
        val totalCompletedSubBab: Int,
        val loginStreak: Int,
        val currentRank: Int? = null,
        val lastAccessedSubBabId: Int = 1,
        val lastAccessedTitle: String = "Ayo mulai belajar Bab 1!"
    ) : DashboardState()
    data class Error(val message: String) : DashboardState()
}

class DashboardViewModel(
    private val siswaRepository: SiswaRepository,
    private val progresRepository: ProgresRepository,
    private val babRepository: BabRepository
) : ViewModel() {

    private val _dashboardState = MutableStateFlow<DashboardState>(DashboardState.Loading)
    val dashboardState: StateFlow<DashboardState> = _dashboardState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            try {
                val siswaId = siswaRepository.currentSiswaId
                if (siswaId == null) {
                    _dashboardState.value = DashboardState.Error("Sesi login tidak valid. Silakan login ulang.")
                    return@launch
                }

                val siswa = siswaRepository.getSiswaData(siswaId)
                if (siswa == null) {
                    _dashboardState.value = DashboardState.Error("Data siswa tidak ditemukan.")
                    return@launch
                }

                combine(
                    babRepository.getAllBab(),
                    progresRepository.getProgresBySiswaId(siswaId),
                    siswaRepository.getAllSiswaByXp()
                ) { babList, progresList, allSiswaByXp ->
                    var totalCompleted = 0
                    var isPreviousBabCompleted = true
                    
                    val evaluatedBabList = babList.map { bab ->
                        var isAllSubBabCompleted = true
                        var isPreviousSubBabCompleted = true

                        val evaluatedSubBabs = bab.listSubBab.map { subBab ->
                            val progres = progresList.find { it.subBabId == subBab.id }
                            val isSelesai = progres?.status == "SELESAI"
                            if (isSelesai) totalCompleted++
                            
                            val isLocked = !isPreviousSubBabCompleted
                            isPreviousSubBabCompleted = isSelesai
                            if (!isSelesai) isAllSubBabCompleted = false

                            subBab.copy(isSelesai = isSelesai, isLocked = isLocked)
                        }

                        val babProgres = progresList.find { it.babId == bab.id }
                        val babSelesai = babProgres?.status == "SELESAI"
                        
                        val babIsLocked = !isPreviousBabCompleted
                        isPreviousBabCompleted = babSelesai

                        bab.copy(
                            listSubBab = evaluatedSubBabs,
                            isSelesai = babSelesai,
                            isLocked = babIsLocked
                        )
                    }
                    
                    val lastAccessedProgres = progresList
                        .filter { it.subBabId != null }
                        .maxByOrNull { it.lastAccessedAt }
                        
                    val lastAccessedSubBabId = lastAccessedProgres?.subBabId ?: 1
                    var lastAccessedTitle = "Ayo mulai belajar Bab 1!"
                    
                    if (lastAccessedProgres != null) {
                        val targetBab = babList.find { b -> b.listSubBab.any { sb -> sb.id == lastAccessedProgres.subBabId } }
                        val targetSubBab = targetBab?.listSubBab?.find { it.id == lastAccessedProgres.subBabId }
                        if (targetSubBab != null) {
                            lastAccessedTitle = "Lanjutkan: ${targetSubBab.judulSubBab}"
                        }
                    }

                    val currentRankIndex = allSiswaByXp.indexOfFirst { it.id == siswaId }
                    val currentRank = if (currentRankIndex != -1) currentRankIndex + 1 else null

                    DashboardState.Success(
                        siswa = siswa, 
                        babList = evaluatedBabList,
                        totalCompletedSubBab = totalCompleted,
                        loginStreak = siswa.loginStreak,
                        currentRank = currentRank,
                        lastAccessedSubBabId = lastAccessedSubBabId,
                        lastAccessedTitle = lastAccessedTitle
                    )
                }.collect { newState ->
                    _dashboardState.value = newState
                }

            } catch (e: Exception) {
                _dashboardState.value = DashboardState.Error("Terjadi kesalahan sistem")
            }
        }
    }

    companion object {
        fun provideFactory(
            siswaRepository: SiswaRepository,
            progresRepository: ProgresRepository,
            babRepository: BabRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                    return DashboardViewModel(siswaRepository, progresRepository, babRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
