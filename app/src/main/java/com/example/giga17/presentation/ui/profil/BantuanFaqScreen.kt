package com.example.giga17.presentation.ui.profil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BantuanFaqScreen(onBack: () -> Unit) {
    val faqs = listOf(
        "Bagaimana cara mendapatkan XP?" to "Anda bisa mendapatkan XP dengan membaca materi, mencoba kode di terminal, dan menyelesaikan kuis.",
        "Apa itu Streak Login?" to "Streak Login adalah hitungan jumlah hari berturut-turut Anda membuka aplikasi dan belajar.",
        "Bagaimana jika aplikasi error?" to "Coba tutup paksa aplikasi dan buka kembali. Jika masih bermasalah, hubungi admin sekolah.",
        "Apakah XP bisa ditukar hadiah?" to "Saat ini XP digunakan untuk memacu motivasi belajar dan menaikkan level Anda."
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bantuan & FAQ", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Pertanyaan Umum",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(faqs) { (question, answer) ->
                FaqItem(question, answer)
            }
        }
    }
}

@Composable
fun FaqItem(question: String, answer: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = answer,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.5f
        )
    }
}
