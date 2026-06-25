package com.example.giga17.presentation.ui.materi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.giga17.presentation.viewmodel.KuisState
import com.example.giga17.presentation.viewmodel.KuisViewModel
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailSubBabScreen(
    subBabId: Int,
    viewModel: KuisViewModel,
    onNavigateToKuis: (Int) -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.kuisState.collectAsStateWithLifecycle()
    
    LaunchedEffect(subBabId) {
        viewModel.loadSubBab(subBabId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Sub-bab") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (val currentState = state) {
                is KuisState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is KuisState.Error -> {
                    Text(text = currentState.message, modifier = Modifier.align(Alignment.Center))
                }
                is KuisState.SubBabDetail -> {
                    val subBab = currentState.subBab
                    val isAlreadyCompleted = currentState.isAlreadyCompleted

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp)
                    ) {
                        Text(
                            text = subBab.judulSubBab,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        MateriRichText(text = subBab.isiTeks)

                        Spacer(modifier = Modifier.height(32.dp))
                        
                        // Removed Interactive Playground from here
                        
                        if (subBab.kuisSubBab != null && subBab.kuisSubBab.questions.isNotEmpty()) {
                            Button(
                                onClick = { 
                                    onNavigateToKuis(subBab.id)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = if (isAlreadyCompleted) "Coba Kuis Lagi" else "Kerjakan Kuis Sub-bab",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        } else {
                            Button(
                                onClick = { 
                                    viewModel.finishTeoriAndGetXP()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                shape = RoundedCornerShape(12.dp),
                                enabled = !isAlreadyCompleted
                            ) {
                                Text(
                                    text = if (isAlreadyCompleted) "Telah Diselesaikan" else "Tandai Selesai",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
                is KuisState.Finished -> {
                    // Handled if we use finishTeoriAndGetXP
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Selesai Membaca",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onBack) {
                            Text("Kembali")
                        }
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
fun MateriRichText(text: String) {
    val imageRegex = """!\[(.*?)\]\((.*?)\)""".toRegex()
    val paragraphs = text.split("\n\n")
    
    Column(modifier = Modifier.fillMaxWidth()) {
        paragraphs.forEach { paragraph ->
            val trimmed = paragraph.trim()
            val matchResult = imageRegex.matchEntire(trimmed)
            if (matchResult != null) {
                val altText = matchResult.groupValues[1]
                val url = matchResult.groupValues[2]
                
                AsyncImage(
                    model = url,
                    contentDescription = altText,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            } else {
                Text(
                    text = trimmed,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
