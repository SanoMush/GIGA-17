package com.example.giga17.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.giga17.data.model.Pencapaian
import com.example.giga17.data.model.Siswa
import com.example.giga17.data.repository.PencapaianRepository
import com.example.giga17.data.repository.SiswaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import com.example.giga17.domain.engine.GamificationEngine

sealed class ProfilState {
    object Loading : ProfilState()
    data class Success(
        val siswa: Siswa,
        val unlockedBadges: List<Pencapaian>,
        val allBadges: List<GamificationEngine.BadgeDef> = GamificationEngine.ALL_AVAILABLE_BADGES
    ) : ProfilState()
    data class Error(val message: String) : ProfilState()
}

class ProfilViewModel(
    private val siswaRepository: SiswaRepository,
    private val pencapaianRepository: PencapaianRepository
) : ViewModel() {

    private val _profilState = MutableStateFlow<ProfilState>(ProfilState.Loading)
    val profilState: StateFlow<ProfilState> = _profilState.asStateFlow()

    init {
        loadProfilData()
    }

    private fun loadProfilData() {
        viewModelScope.launch {
            try {
                val siswaId = siswaRepository.currentSiswaId
                if (siswaId == null) {
                    _profilState.value = ProfilState.Error("Sesi login tidak valid.")
                    return@launch
                }

                val siswa = siswaRepository.getSiswaData(siswaId)
                if (siswa == null) {
                    _profilState.value = ProfilState.Error("Data siswa tidak ditemukan.")
                    return@launch
                }

                pencapaianRepository.getPencapaianBySiswaId(siswaId).collect { unlocked ->
                    _profilState.value = ProfilState.Success(
                        siswa = siswa,
                        unlockedBadges = unlocked
                    )
                }

            } catch (e: Exception) {
                _profilState.value = ProfilState.Error("Terjadi kesalahan: ${e.message}")
            }
        }
    }

    fun logout() {
        siswaRepository.currentSiswaId = null
    }

    fun updateAvatar(avatarName: String) {
        viewModelScope.launch {
            val siswaId = siswaRepository.currentSiswaId ?: return@launch
            siswaRepository.updateAvatar(siswaId, avatarName)
            loadProfilData()
        }
    }

    companion object {
        fun provideFactory(
            siswaRepository: SiswaRepository,
            pencapaianRepository: PencapaianRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ProfilViewModel::class.java)) {
                    return ProfilViewModel(siswaRepository, pencapaianRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
