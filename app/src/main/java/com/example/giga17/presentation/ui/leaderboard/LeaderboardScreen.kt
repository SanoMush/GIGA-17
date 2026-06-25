package com.example.giga17.presentation.ui.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.giga17.data.model.Siswa
import com.example.giga17.presentation.viewmodel.LeaderboardState
import com.example.giga17.presentation.viewmodel.LeaderboardViewModel

@Composable
fun LeaderboardScreen(
    viewModel: LeaderboardViewModel
) {
    val state by viewModel.leaderboardState.collectAsStateWithLifecycle()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Leaderboard",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Top 20 Siswa dengan XP Tertinggi",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }

            when (val currentState = state) {
                is LeaderboardState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is LeaderboardState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = currentState.message, color = MaterialTheme.colorScheme.error)
                    }
                }
                is LeaderboardState.Success -> {
                    val topUsers = currentState.topUsers

                    if (topUsers.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "Belum ada data siswa", color = MaterialTheme.colorScheme.onBackground)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            // Podium section
                            item {
                                Podium(topUsers = topUsers)
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            // Current User Highlight
                            currentState.currentSiswa?.let { siswa ->
                                currentState.currentUserRank?.let { rank ->
                                    item {
                                        MyRankHighlight(siswa = siswa, rank = rank)
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                }
                            }

                            // List of other ranks (Rank 4 to 20)
                            if (topUsers.size > 3) {
                                val remainingUsers = topUsers.subList(3, topUsers.size)
                                itemsIndexed(remainingUsers) { index, siswa ->
                                    val actualRank = index + 4
                                    val isCurrentUser = siswa.id == currentState.currentSiswa?.id
                                    RankListItem(siswa = siswa, rank = actualRank, isCurrentUser = isCurrentUser)
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
fun Podium(topUsers: List<Siswa>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        // Rank 2
        if (topUsers.size > 1) {
            PodiumPillar(siswa = topUsers[1], rank = 2, height = 150.dp, color = Color(0xFFC0C0C0)) // Silver
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        // Rank 1
        if (topUsers.isNotEmpty()) {
            PodiumPillar(siswa = topUsers[0], rank = 1, height = 200.dp, color = Color(0xFFFFD700)) // Gold
        }

        // Rank 3
        if (topUsers.size > 2) {
            PodiumPillar(siswa = topUsers[2], rank = 3, height = 120.dp, color = Color(0xFFCD7F32)) // Bronze
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun RowScope.PodiumPillar(siswa: Siswa, rank: Int, height: Dp, color: Color) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        // Medal Icon
        Icon(
            imageVector = Icons.Default.EmojiEvents,
            contentDescription = "Medal $rank",
            tint = color,
            modifier = Modifier.size(if (rank == 1) 64.dp else 48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        // Avatar / Name
        Text(
            text = siswa.nama.split(" ").firstOrNull() ?: "",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            maxLines = 1,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "${siswa.totalXp} XP",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Pillar
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(height)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(color),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "$rank",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                color = Color.White,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun MyRankHighlight(siswa: Siswa, rank: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.secondary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "#$rank",
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Peringkat Saya",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                )
                Text(
                    text = siswa.nama,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Text(
                text = "${siswa.totalXp} XP",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun RankListItem(siswa: Siswa, rank: Int, isCurrentUser: Boolean) {
    val backgroundColor = if (isCurrentUser) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
    val contentColor = if (isCurrentUser) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isCurrentUser) 4.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$rank",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = contentColor.copy(alpha = 0.7f),
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = contentColor.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(40.dp)
                    .background(contentColor.copy(alpha = 0.1f), CircleShape)
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = siswa.nama,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                    color = contentColor
                )
                Text(
                    text = "Level ${siswa.currentLevel}",
                    style = MaterialTheme.typography.bodySmall,
                    color = contentColor.copy(alpha = 0.7f)
                )
            }
            Text(
                text = "${siswa.totalXp} XP",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = contentColor
            )
        }
    }
}
