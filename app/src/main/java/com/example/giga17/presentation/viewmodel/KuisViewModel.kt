package com.example.giga17.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.giga17.data.model.Bab
import com.example.giga17.data.model.Progres
import com.example.giga17.data.model.SubBab
import com.example.giga17.data.repository.BabRepository
import com.example.giga17.data.repository.ProgresRepository
import com.example.giga17.data.repository.SiswaRepository
import com.example.giga17.domain.engine.GamificationEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class KuisState {
    object Loading : KuisState()
    
    // For SubBab Reading
    data class SubBabDetail(val subBab: SubBab, val isAlreadyCompleted: Boolean) : KuisState()
    
    // For Playing Quiz
    data class Playing(val title: String, val questions: List<QuizQuestion>, val currentQuestionIndex: Int, val score: Int, val isAlreadyCompleted: Boolean, val targetType: String, val targetId: Int, val xpReward: Int, val answerResults: List<AnswerResult> = emptyList()) : KuisState()
    
    // For Finished
    data class Finished(val title: String, val score: Int, val earnedXp: Int, val newLevel: Int, val isAlreadyCompleted: Boolean, val answerResults: List<AnswerResult> = emptyList()) : KuisState()
    
    data class Error(val message: String) : KuisState()
}

data class AnswerResult(
    val question: QuizQuestion,
    val selectedIndex: Int,
    val isCorrect: Boolean
)

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

class KuisViewModel(
    private val babRepository: BabRepository,
    private val progresRepository: ProgresRepository,
    private val siswaRepository: SiswaRepository
) : ViewModel() {

    private val _kuisState = MutableStateFlow<KuisState>(KuisState.Loading)
    val kuisState: StateFlow<KuisState> = _kuisState.asStateFlow()

    private val _isSimulationTried = MutableStateFlow(false)
    val isSimulationTried: StateFlow<Boolean> = _isSimulationTried.asStateFlow()

    fun markSimulationTried() {
        _isSimulationTried.value = true
    }

    fun loadSubBab(subBabId: Int) {
        viewModelScope.launch {
            _isSimulationTried.value = false
            _kuisState.value = KuisState.Loading
            try {
                val subBab = babRepository.getSubBabById(subBabId)
                if (subBab == null) {
                    _kuisState.value = KuisState.Error("SubBab tidak ditemukan")
                    return@launch
                }

                val siswaId = siswaRepository.currentSiswaId ?: return@launch
                val progres = progresRepository.getProgresBySiswaAndSubBab(siswaId, subBab.id)
                val isAlreadyCompleted = progres?.status == "SELESAI"

                _kuisState.value = KuisState.SubBabDetail(subBab, isAlreadyCompleted)
            } catch (e: Exception) {
                _kuisState.value = KuisState.Error("Gagal memuat SubBab: ${e.message}")
            }
        }
    }

    fun startSubBabQuiz(subBabId: Int) {
        viewModelScope.launch {
            _kuisState.value = KuisState.Loading
            try {
                val subBab = babRepository.getSubBabById(subBabId)
                if (subBab == null || subBab.kuisSubBab == null) {
                    _kuisState.value = KuisState.Error("Kuis tidak ditemukan")
                    return@launch
                }
                
                val siswaId = siswaRepository.currentSiswaId ?: return@launch
                val progres = progresRepository.getProgresBySiswaAndSubBab(siswaId, subBab.id)
                val isAlreadyCompleted = progres?.status == "SELESAI"

                _kuisState.value = KuisState.Playing(
                    title = "Kuis: ${subBab.judulSubBab}",
                    questions = subBab.kuisSubBab.questions,
                    currentQuestionIndex = 0,
                    score = 0,
                    isAlreadyCompleted = isAlreadyCompleted,
                    targetType = "SUBBAB",
                    targetId = subBab.id,
                    xpReward = subBab.kuisSubBab.xpReward
                )
            } catch (e: Exception) {
                _kuisState.value = KuisState.Error("Gagal memuat Kuis: ${e.message}")
            }
        }
    }

    fun startBabQuiz(babId: Int) {
        viewModelScope.launch {
            _kuisState.value = KuisState.Loading
            try {
                val bab = babRepository.getBabById(babId)
                if (bab == null || bab.kuisAkhirBab == null) {
                    _kuisState.value = KuisState.Error("Kuis Akhir Bab tidak ditemukan")
                    return@launch
                }
                
                val siswaId = siswaRepository.currentSiswaId ?: return@launch
                val progres = progresRepository.getProgresBySiswaAndBab(siswaId, bab.id)
                val isAlreadyCompleted = progres?.status == "SELESAI"

                _kuisState.value = KuisState.Playing(
                    title = "Kuis Akhir Bab: ${bab.judulBab}",
                    questions = bab.kuisAkhirBab.questions,
                    currentQuestionIndex = 0,
                    score = 0,
                    isAlreadyCompleted = isAlreadyCompleted,
                    targetType = "BAB",
                    targetId = bab.id,
                    xpReward = bab.kuisAkhirBab.xpReward
                )
            } catch (e: Exception) {
                _kuisState.value = KuisState.Error("Gagal memuat Kuis Akhir Bab: ${e.message}")
            }
        }
    }

    fun goToPreviousQuestion() {
        val state = _kuisState.value
        if (state is KuisState.Playing && state.currentQuestionIndex > 0) {
            val lastAnswer = state.answerResults.lastOrNull()
            val scoreToSubtract = if (lastAnswer?.isCorrect == true) (100 / state.questions.size) else 0
            
            _kuisState.value = state.copy(
                currentQuestionIndex = state.currentQuestionIndex - 1,
                score = state.score - scoreToSubtract,
                answerResults = state.answerResults.dropLast(1)
            )
        }
    }

    fun submitAnswer(selectedIndex: Int) {
        val state = _kuisState.value
        if (state is KuisState.Playing) {
            val currentQ = state.questions[state.currentQuestionIndex]
            val isCorrect = selectedIndex == currentQ.correctIndex
            val newScore = if (isCorrect) state.score + (100 / state.questions.size) else state.score
            
            val newAnswerResults = state.answerResults + AnswerResult(currentQ, selectedIndex, isCorrect)

            if (state.currentQuestionIndex + 1 < state.questions.size) {
                // Next question
                _kuisState.value = state.copy(
                    currentQuestionIndex = state.currentQuestionIndex + 1,
                    score = newScore,
                    answerResults = newAnswerResults
                )
            } else {
                // Finish quiz
                finishQuiz(state.title, state.targetType, state.targetId, state.xpReward, newScore, newAnswerResults)
            }
        }
    }

    fun finishTeoriAndGetXP() {
        val state = _kuisState.value
        if (state is KuisState.SubBabDetail) {
            // For theory reading, we just give 100 score and trigger progress save.
            finishQuiz(state.subBab.judulSubBab, "SUBBAB_TEORI", state.subBab.id, 10, 100, emptyList()) // Dummy XP 10 for reading if no quiz exists
        }
    }

    private fun finishQuiz(title: String, targetType: String, targetId: Int, baseXpReward: Int, finalScore: Int, answerResults: List<AnswerResult>) {
        viewModelScope.launch {
            try {
                val siswaId = siswaRepository.currentSiswaId ?: return@launch
                val siswa = siswaRepository.getSiswaData(siswaId) ?: return@launch
                
                var progres = if (targetType == "BAB") {
                    progresRepository.getProgresBySiswaAndBab(siswaId, targetId)
                } else {
                    progresRepository.getProgresBySiswaAndSubBab(siswaId, targetId)
                }
                
                val isAlreadyCompleted = progres?.status == "SELESAI"
                val attempt = if (progres == null) 1 else progres.attempt + 1
                
                // Calculate XP
                val earnedXp = if (isAlreadyCompleted) 0 else if (targetType == "SUBBAB_TEORI") 20 else 50
                
                // Update Progres
                val subBabIdToSave = if (targetType == "BAB") null else targetId
                val babIdToSave = if (targetType == "BAB") targetId else null

                if (progres == null) {
                    progres = Progres(
                        id = 0,
                        siswaId = siswaId,
                        subBabId = subBabIdToSave,
                        babId = babIdToSave,
                        skor = finalScore,
                        waktuPengerjaanDetik = 0,
                        status = "SELESAI",
                        attempt = attempt,
                        xpEarned = earnedXp,
                        completedAt = System.currentTimeMillis(),
                        lastAccessedAt = System.currentTimeMillis()
                    )
                } else {
                    progres = progres.copy(
                        status = "SELESAI",
                        skor = maxOf(progres.skor, finalScore),
                        attempt = attempt,
                        xpEarned = if (isAlreadyCompleted) progres.xpEarned else earnedXp,
                        completedAt = if (isAlreadyCompleted) progres.completedAt else System.currentTimeMillis(),
                        lastAccessedAt = System.currentTimeMillis()
                    )
                }
                progresRepository.saveProgres(progres)

                // Update Siswa XP & Level
                var newLevel = siswa.currentLevel
                if (!isAlreadyCompleted && earnedXp > 0) {
                    siswaRepository.addXp(earnedXp)
                    newLevel = GamificationEngine.calculateLevel(siswa.totalXp + earnedXp)
                }

                if (!isAlreadyCompleted && subBabIdToSave != null) {
                    siswaRepository.markSubBabComplete(subBabIdToSave.toString())
                }

                _kuisState.value = KuisState.Finished(title, finalScore, earnedXp, newLevel, isAlreadyCompleted, answerResults)
            } catch (e: Exception) {
                _kuisState.value = KuisState.Error("Gagal menyimpan progres")
            }
        }
    }

    companion object {
        fun provideFactory(
            babRepository: BabRepository,
            progresRepository: ProgresRepository,
            siswaRepository: SiswaRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(KuisViewModel::class.java)) {
                    return KuisViewModel(babRepository, progresRepository, siswaRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
