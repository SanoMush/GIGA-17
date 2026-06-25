package com.example.giga17.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.giga17.data.repository.CompilerRepository
import com.example.giga17.data.repository.SiswaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TerminalViewModel(
    private val compilerRepository: CompilerRepository,
    private val siswaRepository: SiswaRepository
) : ViewModel() {

    private val _kodeInput = MutableStateFlow("program Hello;\nbegin\n  writeln('Hello, Pascal!');\nend.")
    val kodeInput: StateFlow<String> = _kodeInput.asStateFlow()

    private val _outputTerminal = MutableStateFlow("Output Terminal:\n> _")
    val outputTerminal: StateFlow<String> = _outputTerminal.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun updateKodeInput(newCode: String) {
        _kodeInput.value = newCode
    }

    fun compileCode() {
        if (_isLoading.value) return
        
        viewModelScope.launch {
            _isLoading.value = true
            _outputTerminal.value = "Sedang menghubungi server..."
            
            val result = compilerRepository.compilePascal(_kodeInput.value)
            
            _outputTerminal.value = result
            
            val lowerResult = result.lowercase()
            if (!lowerResult.contains("error") && !lowerResult.contains("gagal") && !lowerResult.contains("exception")) {
                siswaRepository.addXp(10)
                _outputTerminal.value += "\n\n[Sistem] +10 XP karena kode berhasil dijalankan!"
            }
            
            _isLoading.value = false
        }
    }

    companion object {
        fun provideFactory(
            compilerRepository: CompilerRepository,
            siswaRepository: SiswaRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(TerminalViewModel::class.java)) {
                    return TerminalViewModel(compilerRepository, siswaRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
