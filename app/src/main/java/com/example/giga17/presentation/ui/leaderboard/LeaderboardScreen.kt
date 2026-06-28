package com.example.giga17.presentation.ui.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.giga17.data.model.Siswa
import com.example.giga17.presentation.viewmodel.LeaderboardState
import com.example.giga17.presentation.viewmodel.LeaderboardViewModel

@Composable
fun LeaderboardScreen(
    viewModel: LeaderboardViewModel,
    onNavigateToMateri: () -> Unit = {}
) {
    val state by viewModel.leaderboardState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
    ) {
        // App Bar
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Leaderboard",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(24.dp))

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
                        Text(text = "Belum ada data siswa", color = Color.Gray)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        // Podium section
                        item {
                            PodiumSection(topUsers = topUsers)
                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        // List Header
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Rank",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFF8A8A8E),
                                    modifier = Modifier.width(40.dp)
                                )
                                Text(
                                    text = "Siswa",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFF8A8A8E),
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "XP",
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFF8A8A8E)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Users List
                        if (topUsers.size > 3) {
                            val remainingUsers = topUsers.subList(3, topUsers.size)
                            itemsIndexed(remainingUsers) { index, siswa ->
                                val actualRank = index + 4
                                val isCurrentUser = siswa.id == currentState.currentSiswa?.id
                                val isKamu = isCurrentUser || (index == 1) // Fallback for UI testing just in case
                                LeaderboardRow(
                                    siswa = siswa,
                                    rank = actualRank,
                                    isCurrentUser = isCurrentUser
                                )
                            }
                        }

                        // Motivation Card
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                            MotivationCard(onClick = onNavigateToMateri)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PodiumSection(topUsers: List<Siswa>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // Rank 2
            if (topUsers.size > 1) {
                PodiumCard(
                    siswa = topUsers[1],
                    rank = 2,
                    height = 170.dp,
                    cardBg = Color(0xFFF3F0FF),
                    bottomColor = Color(0xFF4ADE80), // Green
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            // Rank 1
            if (topUsers.isNotEmpty()) {
                PodiumCard(
                    siswa = topUsers[0],
                    rank = 1,
                    height = 200.dp,
                    cardBg = Color(0xFFFFF9E6),
                    bottomColor = Color(0xFFFFC107), // Yellow/Gold
                    modifier = Modifier.weight(1f),
                    isFirst = true
                )
            }

            // Rank 3
            if (topUsers.size > 2) {
                PodiumCard(
                    siswa = topUsers[2],
                    rank = 3,
                    height = 150.dp,
                    cardBg = Color(0xFFFFF0E6),
                    bottomColor = Color(0xFFFF9800), // Orange
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun PodiumCard(
    siswa: Siswa,
    rank: Int,
    height: Dp,
    cardBg: Color,
    bottomColor: Color,
    modifier: Modifier = Modifier,
    isFirst: Boolean = false
) {
    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(20.dp))
            .background(cardBg),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isFirst) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Crown",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .size(32.dp)
                        .offset(y = (-16).dp)
                )
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Avatar Placeholder (Circle with initial or generic icon)
            Box(
                modifier = Modifier
                    .size(if (isFirst) 64.dp else 56.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFE2E8F0), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = Color.Gray,
                        modifier = Modifier.size(if (isFirst) 40.dp else 32.dp)
                    )
                }
                
                // Rank Badge overlapping avatar
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(x = (-8).dp, y = (-8).dp)
                        .size(24.dp)
                        .background(MaterialTheme.colorScheme.surface, CircleShape)
                        .shadow(2.dp, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$rank",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Name
            val firstName = siswa.nama.split(" ").firstOrNull() ?: ""
            Text(
                text = firstName,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
            
            // XP
            Text(
                text = "${siswa.totalXp} XP",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = bottomColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Colored Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
                    .background(bottomColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Juara $rank",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun LeaderboardRow(siswa: Siswa, rank: Int, isCurrentUser: Boolean) {
    val bgColor = if (isCurrentUser) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) else Color.Transparent
    val xpColor = if (isCurrentUser) Color(0xFFFF7A3D) else MaterialTheme.colorScheme.onBackground
    val nameColor = if (isCurrentUser) Color(0xFFFF7A3D) else MaterialTheme.colorScheme.onBackground
    val displayName = if (isCurrentUser) "Kamu" else siswa.nama

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$rank",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.width(40.dp)
        )
        
        // Avatar
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFFE2E8F0), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = displayName,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = nameColor,
            modifier = Modifier.weight(1f),
            maxLines = 1
        )
        
        Text(
            text = "${siswa.totalXp} XP",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = xpColor
        )
    }
}

@Composable
fun MotivationCard(onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.EmojiEvents,
            contentDescription = "Trophy",
            tint = Color(0xFFFF7A3D),
            modifier = Modifier.size(48.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Belajar lebih konsisten untuk",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Text(
                text = "naik peringkat!",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFFFF7A3D)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Terus kumpulkan XP dan jadi yang terbaik!",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = Color.LightGray
            )
        }
        
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color(0xFFFF7A3D).copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Go",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
