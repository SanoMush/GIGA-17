package com.example.giga17.presentation.ui.kuis

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.giga17.presentation.viewmodel.KuisState
import com.example.giga17.presentation.viewmodel.KuisViewModel

@Composable
fun KuisScreen(
    targetType: String,
    targetId: Int,
    viewModel: KuisViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.kuisState.collectAsStateWithLifecycle()

    LaunchedEffect(targetType, targetId) {
        if (targetType == "BAB") {
            viewModel.startBabQuiz(targetId)
        } else {
            viewModel.startSubBabQuiz(targetId)
        }
    }

    BackHandler(enabled = state is KuisState.Playing) {
        // Mencegah user keluar menggunakan tombol back/back gesture saat kuis berlangsung
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 24.dp, top = 12.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (state !is KuisState.Playing) {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (state is KuisState.Playing) (state as KuisState.Playing).title else "Memuat Kuis...",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (val s = state) {
                    is KuisState.Playing -> {
                        val question = s.questions[s.currentQuestionIndex]
                    
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp)
                        ) {
                            Text(
                                text = "Soal ${s.currentQuestionIndex + 1} / ${s.questions.size}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = question.question,
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                            
                            question.options.forEachIndexed { index, option ->
                                Button(
                                    onClick = { viewModel.submitAnswer(index) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 12.dp)
                                        .height(56.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        contentColor = MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text(
                                        text = option,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }

                            if (s.currentQuestionIndex > 0) {
                                Spacer(modifier = Modifier.weight(1f))
                                OutlinedButton(
                                    onClick = { viewModel.goToPreviousQuestion() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "Kembali ke Soal Sebelumnya",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }
                    is KuisState.Finished -> {
                        val correctCount = s.answerResults.count { it.isCorrect }
                        val wrongCount = s.answerResults.count { !it.isCorrect }
                        val wrongAnswers = s.answerResults.filter { !it.isCorrect }

                        androidx.compose.foundation.lazy.LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            item {
                                Text(
                                    text = "Kuis Selesai!",
                                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(text = "Skor: ${s.score}", style = MaterialTheme.typography.titleLarge)
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = "Benar: $correctCount | Salah: $wrongCount",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                if (s.isAlreadyCompleted) {
                                    Text(
                                        text = "Latihan selesai!\n(Poin XP hanya diberikan pada percobaan pertama)",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.padding(horizontal = 24.dp),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                } else {
                                    Text(text = "+${s.earnedXp} XP", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.secondary)
                                }
                                
                                Spacer(modifier = Modifier.height(24.dp))
                            }

                            if (wrongAnswers.isNotEmpty()) {
                                item {
                                    Text(
                                        text = "Evaluasi Jawaban Salah:",
                                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Start
                                    )
                                }

                                items(wrongAnswers.size) { index ->
                                    val result = wrongAnswers[index]
                                    Card(
                                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text(
                                                text = result.question.question,
                                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "Jawaban Anda: ${if(result.selectedIndex >= 0) result.question.options[result.selectedIndex] else "Kosong"}",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                            Text(
                                                text = "Jawaban Benar: ${result.question.options[result.question.correctIndex]}",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = androidx.compose.ui.graphics.Color(0xFF2E7D32) // Dark Green
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "Penjelasan: ${result.question.explanation}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(24.dp))
                                Button(
                                    onClick = onBack,
                                    modifier = Modifier.fillMaxWidth(0.8f).height(50.dp)
                                ) {
                                    Text("Kembali")
                                }
                            }
                        }
                    }
                    is KuisState.Error -> {
                        Text(
                            text = s.message, 
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    else -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}
