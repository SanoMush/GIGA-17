package com.example.giga17.presentation.ui.profil

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.giga17.domain.engine.GamificationEngine
import com.example.giga17.domain.engine.GamificationEngine.BadgeDef
import com.example.giga17.presentation.viewmodel.ProfilState
import com.example.giga17.presentation.viewmodel.ProfilViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilScreen(
    viewModel: ProfilViewModel,
    onLogout: () -> Unit,
    onBack: () -> Unit,
    onNavigateToGaleri: () -> Unit = {},
    onNavigateToNotifikasi: () -> Unit = {},
    onNavigateToPrivasi: () -> Unit = {},
    onNavigateToBantuan: () -> Unit = {}
) {
    val state by viewModel.profilState.collectAsStateWithLifecycle()
    var showAvatarDialog by remember { mutableStateOf(false) }

    val avatarOptions = listOf(
        "Person" to Icons.Default.Person,
        "Face" to Icons.Default.Face,
        "Star" to Icons.Default.Star,
        "Build" to Icons.Default.Build,
        "Info" to Icons.Default.Info,
        "ThumbUp" to Icons.Default.ThumbUp
    )

    if (showAvatarDialog) {
        AlertDialog(
            onDismissRequest = { showAvatarDialog = false },
            title = { Text("Pilih Avatar") },
            text = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(avatarOptions) { (name, icon) ->
                        IconButton(
                            onClick = {
                                viewModel.updateAvatar(name)
                                showAvatarDialog = false
                            },
                            modifier = Modifier
                                .size(64.dp)
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f), CircleShape)
                        ) {
                            Icon(imageVector = icon, contentDescription = name, tint = Color(0xFFFF7A3D), modifier = Modifier.size(32.dp))
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAvatarDialog = false }) {
                    Text("Tutup", color = Color(0xFFFF7A3D))
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        // Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(
                    text = "Profil",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Kelola informasi dan pencapaianmu",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { 
                        viewModel.logout()
                        onLogout()
                    }
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Logout",
                    tint = Color(0xFFFF7A3D),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Logout",
                    color = Color(0xFFFF7A3D),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (val s = state) {
            is ProfilState.Loading -> {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFFF7A3D))
                }
            }
            is ProfilState.Error -> {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    Text(text = s.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is ProfilState.Success -> {
                // User Info Card
                UserInfoCard(s = s, onEditAvatar = { showAvatarDialog = true })
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Achievements Gallery
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Galeri Pencapaian",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Lihat semua",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFFF7A3D),
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { onNavigateToGaleri() }
                            .padding(4.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(s.allBadges) { badgeDef ->
                        val isUnlocked = s.unlockedBadges.any { it.badgeId == badgeDef.id }
                        AchievementBadgeItem(badgeDef, isUnlocked)
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Settings List
                SettingsMenu(
                    onNavigateToNotifikasi = onNavigateToNotifikasi,
                    onNavigateToPrivasi = onNavigateToPrivasi,
                    onNavigateToBantuan = onNavigateToBantuan
                )
                
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun UserInfoCard(s: ProfilState.Success, onEditAvatar: () -> Unit) {
    val currentAvatarIcon = when (s.siswa.avatarResId) {
        "Face" -> Icons.Default.Face
        "Star" -> Icons.Default.Star
        "Build" -> Icons.Default.Build
        "Info" -> Icons.Default.Info
        "ThumbUp" -> Icons.Default.ThumbUp
        else -> Icons.Default.Person
    }

    // Hitung next level XP secara sederhana
    val currentLevelIndex = s.siswa.currentLevel
    val nextLevelXp = GamificationEngine.levelThresholds.getOrNull(currentLevelIndex) ?: (s.siswa.totalXp + 1000)
    val xpNeeded = (nextLevelXp - s.siswa.totalXp).coerceAtLeast(0)
    val progress = if (nextLevelXp > 0) s.siswa.totalXp.toFloat() / nextLevelXp.toFloat() else 0f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant) // Sangat soft gray/white di light mode, soft dark di dark mode
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar
                Box(modifier = Modifier.size(90.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = currentAvatarIcon,
                            contentDescription = "Avatar",
                            modifier = Modifier.size(50.dp),
                            tint = Color(0xFFFF7A3D)
                        )
                    }
                    
                    // Edit Icon
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = (-4).dp, y = (-4).dp)
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .clickable { onEditAvatar() }
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color(0xFFFF7A3D),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.width(20.dp))
                
                Column {
                    val firstName = s.siswa.nama.split(" ").firstOrNull() ?: ""
                    Text(
                        text = firstName,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "NIS: ${s.siswa.nim}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy NIS",
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Badge Level
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.StarBorder,
                                contentDescription = null,
                                tint = Color(0xFFFF7A3D),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Level ${s.siswa.currentLevel}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        
                        // Badge Status
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF4ADE80).copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = Color(0xFF4ADE80),
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Pelajar Aktif",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // XP Progress
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "XP Progress",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Gray
                )
                Text(
                    text = "${s.siswa.totalXp} / $nextLevelXp XP",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Gray
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Custom Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFE2E8F0))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFFF7A3D))
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "$xpNeeded XP menuju Level ${s.siswa.currentLevel + 1}",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun AchievementBadgeItem(badgeDef: BadgeDef, isUnlocked: Boolean) {
    val iconColor = when (badgeDef.category) {
        "LEVEL" -> Color(0xFFFFD700) // Yellow
        "STREAK" -> Color(0xFFFF9800) // Orange
        "MATERI" -> Color(0xFF4ADE80) // Green
        "KUIS" -> Color(0xFFFFC107) // Gold
        else -> Color(0xFFFF7A3D)
    }
    
    val displayIconColor = if (isUnlocked) iconColor else Color(0xFF94A3B8)
    val statusText = if (isUnlocked) "Diraih" else "Belum Diraih"
    val statusColor = if (isUnlocked) Color(0xFF4ADE80) else Color.LightGray

    val iconVector = when (badgeDef.icon) {
        "Star" -> Icons.Default.Star
        "Face" -> Icons.Default.Face
        "Build" -> Icons.Default.Build
        "Info" -> Icons.Default.Info
        "ThumbUp" -> Icons.Default.ThumbUp
        else -> Icons.Default.EmojiEvents
    }

    Column(
        modifier = Modifier.width(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon Container
        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = iconVector,
                contentDescription = badgeDef.name,
                tint = displayIconColor,
                modifier = Modifier.size(40.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = badgeDef.name,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        
        Text(
            text = statusText,
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
            color = statusColor,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SettingsMenu(
    onNavigateToNotifikasi: () -> Unit,
    onNavigateToPrivasi: () -> Unit,
    onNavigateToBantuan: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        SettingsItem(
            icon = Icons.Default.NotificationsNone,
            title = "Pengaturan Notifikasi",
            iconTint = Color(0xFFFF7A3D),
            onClick = onNavigateToNotifikasi
        )
        SettingsItem(
            icon = Icons.Default.Shield,
            title = "Privasi & Keamanan",
            iconTint = Color(0xFF4ADE80),
            onClick = onNavigateToPrivasi
        )
        SettingsItem(
            icon = Icons.AutoMirrored.Filled.HelpOutline,
            title = "Bantuan & FAQ",
            iconTint = Color(0xFFFFC107),
            showDivider = false,
            onClick = onNavigateToBantuan
        )
    }
}

@Composable
fun SettingsItem(icon: ImageVector, title: String, iconTint: Color, showDivider: Boolean = true, onClick: () -> Unit = {}) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Go",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
        if (showDivider) {
            HorizontalDivider(color = Color(0xFFF1F5F9))
        }
    }
}
