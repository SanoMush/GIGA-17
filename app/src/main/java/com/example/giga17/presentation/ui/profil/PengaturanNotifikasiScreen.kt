package com.example.giga17.presentation.ui.profil

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PengaturanNotifikasiScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var pushNotificationEnabled by remember { mutableStateOf(true) }
    var streakReminderEnabled by remember { mutableStateOf(true) }

    // Efek ketika toggle streak pengingat berubah
    LaunchedEffect(streakReminderEnabled) {
        if (streakReminderEnabled) {
            NotificationHelper.scheduleDailyReminder(context)
        } else {
            NotificationHelper.cancelDailyReminder(context)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifikasi", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text(
                text = "Kategori Notifikasi",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF11142D)
            )
            Spacer(modifier = Modifier.height(16.dp))
            NotificationSwitchItem("Pengingat Streak Belajar (08:00 Pagi)", streakReminderEnabled) { streakReminderEnabled = it }
        }
    }
}

@Composable
fun NotificationSwitchItem(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF11142D)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFFFF7A3D))
        )
    }
    HorizontalDivider(color = Color(0xFFF1F5F9))
}
