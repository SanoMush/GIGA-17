package com.example.giga17.presentation.ui.materi

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulasiScreen(
    babId: Int,
    onBack: () -> Unit,
    onSimulasiTried: () -> Unit = {}
) {
    // For simplicity, we hardcode the mapping since we know the Bab IDs
    val judul = when (babId) {
        1 -> "Simulasi Kotak Memori"
        2 -> "Simulasi Saklar Logika"
        3 -> "Simulasi Mesin Cetak Loop"
        4 -> "Simulasi Loker Indeks"
        else -> "Simulasi"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(judul) },
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (babId) {
                    1 -> KotakMemoriSimulasi(onSimulasiTried)
                    2 -> SaklarLogikaSimulasi(onSimulasiTried)
                    3 -> MesinCetakLoopSimulasi(onSimulasiTried)
                    4 -> LokerIndeksSimulasi(onSimulasiTried)
                    else -> Text("Simulasi tidak tersedia untuk Bab ini.")
                }
            }
        }
    }
}

@Composable
fun KotakMemoriSimulasi(onTried: () -> Unit = {}) {
    var input by remember { mutableStateOf("") }
    var savedValue by remember { mutableStateOf<String?>(null) }
    var typeLabel by remember { mutableStateOf("Kosong") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Masukkan nilai (angka atau teks):",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Nilai") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            savedValue = input
            typeLabel = if (input.toIntOrNull() != null) "Integer"
            else if (input.toFloatOrNull() != null) "Float"
            else if (input.equals("true", ignoreCase = true) || input.equals("false", ignoreCase = true)) "Boolean"
            else "String"
            onTried()
        }) {
            Text("Simpan ke Memori")
        }

        Spacer(modifier = Modifier.height(32.dp))

        AnimatedVisibility(visible = savedValue != null) {
            Card(
                modifier = Modifier
                    .size(200.dp)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Tipe: ${typeLabel}", style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = savedValue ?: "",
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun SaklarLogikaSimulasi(onTried: () -> Unit = {}) {
    var isTrue by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Kondisi If:", style = MaterialTheme.typography.titleMedium)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("False")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(checked = isTrue, onCheckedChange = { 
                isTrue = it 
                onTried()
            })
            Spacer(modifier = Modifier.width(8.dp))
            Text("True")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isTrue) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = "Blok IF Dieksekusi",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = if (isTrue) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (!isTrue) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = "Blok ELSE Dieksekusi",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = if (!isTrue) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun MesinCetakLoopSimulasi(onTried: () -> Unit = {}) {
    var loopCount by remember { mutableStateOf(1f) }
    var printedItems by remember { mutableStateOf(listOf<String>()) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        if (isRunning) {
            printedItems = emptyList()
            for (i in 1..loopCount.toInt()) {
                delay(500)
                printedItems = printedItems + "Cetak baris ke-$i"
            }
            isRunning = false
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Jumlah Iterasi: ${loopCount.toInt()}")
        Slider(
            value = loopCount,
            onValueChange = { loopCount = it },
            valueRange = 1f..10f,
            steps = 8,
            enabled = !isRunning
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { 
            isRunning = true 
            onTried()
        }, enabled = !isRunning) {
            Text("Jalankan Loop")
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
                printedItems.forEach { item ->
                    Text(item)
                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                }
                if (isRunning) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp))
                }
            }
        }
    }
}

@Composable
fun LokerIndeksSimulasi(onTried: () -> Unit = {}) {
    var inputIndex by remember { mutableStateOf("") }
    var inputValue by remember { mutableStateOf("") }
    var arrayState by remember { mutableStateOf(List(5) { "Kosong" }) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            arrayState.forEachIndexed { index, value ->
                Card(
                    modifier = Modifier.weight(1f).padding(4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("[$index]", style = MaterialTheme.typography.labelSmall)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(value, style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = inputIndex,
            onValueChange = { inputIndex = it },
            label = { Text("Indeks (0-4)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            label = { Text("Nilai Baru") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val idx = inputIndex.toIntOrNull()
            if (idx != null && idx in 0..4) {
                val newList = arrayState.toMutableList()
                newList[idx] = if (inputValue.isBlank()) "Kosong" else inputValue
                arrayState = newList
                onTried()
            }
        }) {
            Text("Simpan ke Array")
        }
    }
}
