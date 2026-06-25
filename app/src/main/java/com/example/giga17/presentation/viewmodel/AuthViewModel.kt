package com.example.giga17.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.giga17.data.model.Siswa
import com.example.giga17.data.repository.SiswaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.FirebaseAuth

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val siswa: Siswa) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val siswaRepository: SiswaRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(nim: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val cleanNim = nim.trim()
                val email = "${cleanNim}@sma17.edu"
                val authResult = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
                
                if (authResult.user != null) {
                    try {
                        val siswa = siswaRepository.syncSiswaProfileFromFirebase(nim)
                        _authState.value = AuthState.Success(siswa)
                    } catch (e: Exception) {
                        _authState.value = AuthState.Error(e.message ?: "Gagal menyinkronkan profil dari cloud")
                    }
                } else {
                    _authState.value = AuthState.Error("Kredensial tidak valid")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Terjadi kesalahan sistem")
            }
        }
    }

    companion object {
        fun provideFactory(siswaRepository: SiswaRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                        return AuthViewModel(siswaRepository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
