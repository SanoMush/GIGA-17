package com.example.giga17.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.giga17.data.model.Siswa
import com.example.giga17.data.repository.SiswaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import kotlinx.coroutines.flow.first
import com.example.giga17.data.repository.BabRepository
import com.example.giga17.data.repository.ProgresRepository

class GamificationViewModel(
    private val siswaRepository: SiswaRepository,
    private val progresRepository: ProgresRepository,
    private val babRepository: BabRepository
) : ViewModel() {

    val currentUser: StateFlow<Siswa?> = siswaRepository.observeCurrentSiswa()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun addXp(amount: Int) {
        viewModelScope.launch {
            siswaRepository.addXp(amount)
        }
    }

    fun markSubBabComplete(subBabId: String) {
        viewModelScope.launch {
            siswaRepository.markSubBabComplete(subBabId)
        }
    }

    fun syncUserProgress() {
        viewModelScope.launch {
            val siswa = currentUser.value ?: return@launch
            
            // Dapatkan list sub-bab yang sudah komplit dari profil
            val completedSubBabs = siswa.completedSubBabs
            
            if (completedSubBabs.isNotEmpty()) {
                val progresList = progresRepository.getProgresListBySiswaIdSync(siswa.id)
                val localCompletedIds = progresList.filter { it.status == "SELESAI" && it.subBabId != null }.map { it.subBabId.toString() }
                
                // Cari apakah ada subbab di completedSubBabs yang belum SELESAI secara lokal
                val missingProgress = completedSubBabs.filter { !localCompletedIds.contains(it) }
                
                if (missingProgress.isNotEmpty()) {
                    // Lakukan auto-repair dengan transaction untuk ID yang belum komplit secara lokal
                    val babs = babRepository.getAllBab().first()
                    progresRepository.repairProgressWithTransaction(siswa.id, completedSubBabs, babs)
                }
            }
        }
    }

    companion object {
        fun provideFactory(
            siswaRepository: SiswaRepository,
            progresRepository: ProgresRepository,
            babRepository: BabRepository
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(GamificationViewModel::class.java)) {
                        return GamificationViewModel(siswaRepository, progresRepository, babRepository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
