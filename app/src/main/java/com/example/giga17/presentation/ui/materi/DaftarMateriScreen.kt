package com.example.giga17.presentation.ui.materi

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.giga17.data.model.Bab
import com.example.giga17.data.model.SubBab
import com.example.giga17.presentation.viewmodel.DashboardState
import com.example.giga17.presentation.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarMateriScreen(
    viewModel: DashboardViewModel,
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToBabQuiz: (Int) -> Unit,
    onNavigateToSimulasi: (Int) -> Unit
) {
    val dashboardState by viewModel.dashboardState.collectAsStateWithLifecycle()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                Text(
                    text = "Daftar Materi",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(24.dp)
                )
            }
            
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                when (val state = dashboardState) {
                    is DashboardState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    is DashboardState.Error -> Text(text = state.message, modifier = Modifier.align(Alignment.Center))
                    is DashboardState.Success -> {
                        if (state.babList.isEmpty()) {
                            Text(
                                text = "Belum ada materi",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 24.dp, vertical = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                contentPadding = PaddingValues(bottom = 100.dp)
                            ) {
                                items(state.babList) { bab ->
                                    BabAccordion(
                                        bab = bab,
                                        onSubBabClick = { subBabId ->
                                            onNavigateToDetail(subBabId)
                                        },
                                        onKuisAkhirBabClick = { babId ->
                                            onNavigateToBabQuiz(babId)
                                        },
                                        onSimulasiClick = { babId ->
                                            onNavigateToSimulasi(babId)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BabAccordion(
    bab: Bab,
    onSubBabClick: (Int) -> Unit,
    onKuisAkhirBabClick: (Int) -> Unit,
    onSimulasiClick: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    // Check if all subbabs are completed to unlock the final quiz
    val isAllSubBabCompleted = bab.listSubBab.all { it.isSelesai }
    
    val babOpacity = if (bab.isLocked) 0.5f else 1f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(babOpacity)
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !bab.isLocked) { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (bab.isLocked) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Terkunci",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text(
                            text = "BAB",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = bab.judulBab,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (bab.isLocked) "Selesaikan Kuis Akhir Bab sebelumnya" else "${bab.listSubBab.size} Sub-bab • Kuis Akhir Bab",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (bab.isLocked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                if (!bab.isLocked) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand/Collapse",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Content
            if (expanded) {
                Divider(color = MaterialTheme.colorScheme.surfaceVariant, thickness = 1.dp)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    bab.listSubBab.forEach { subBab ->
                        SubBabItem(
                            subBab = subBab,
                            onClick = { onSubBabClick(subBab.id) }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    val opacity = if (isAllSubBabCompleted) 1f else 0.5f

                    // Simulasi Interaktif Button
                    if (bab.tipeSimulasi != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .alpha(opacity)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.tertiaryContainer)
                                .clickable(enabled = isAllSubBabCompleted) {
                                    onSimulasiClick(bab.id)
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!isAllSubBabCompleted) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Terkunci",
                                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Simulasi: ${bab.tipeSimulasi}",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                                Text(
                                    text = if (isAllSubBabCompleted) "Siap Dimainkan" else "Selesaikan semua Sub-bab dulu",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                                )
                            }
                            if (isAllSubBabCompleted) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Mulai Simulasi",
                                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Kuis Akhir Bab Button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .alpha(opacity)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .clickable(enabled = isAllSubBabCompleted) {
                                onKuisAkhirBabClick(bab.id)
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!isAllSubBabCompleted) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Terkunci",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Kuis Akhir Bab",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = if (isAllSubBabCompleted) "Siap Dikerjakan" else "Selesaikan semua Sub-bab dulu",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                            )
                        }
                        if (isAllSubBabCompleted) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Mulai Kuis",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubBabItem(
    subBab: SubBab,
    onClick: () -> Unit
) {
    val opacity = if (subBab.isLocked) 0.5f else 1f
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(opacity)
            .clickable(enabled = !subBab.isLocked) { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val iconTint = if (subBab.isSelesai) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        
        if (subBab.isLocked) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Terkunci",
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Default.PlayArrow, // You can replace with check icon if completed
                contentDescription = "Materi",
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = subBab.judulSubBab,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = if (subBab.isSelesai) FontWeight.Normal else FontWeight.Medium),
                color = if (subBab.isSelesai) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = if (subBab.isLocked) "Selesaikan materi sebelumnya" else if (subBab.isSelesai) "Selesai" else "Belum dipelajari",
                style = MaterialTheme.typography.bodySmall,
                color = iconTint
            )
        }
    }
}
