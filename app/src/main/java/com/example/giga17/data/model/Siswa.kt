package com.example.giga17.data.model

import com.example.giga17.data.local.entity.SiswaEntity

data class Siswa(
    val id: Int,
    val nim: String,
    val nama: String,
    val kelas: String,
    val avatarResId: String,
    val totalXp: Int,
    val currentLevel: Int,
    val loginStreak: Int,
    val createdAt: Long,
    val lastActiveAt: Long,
    val completedSubBabs: List<String> = emptyList(),
    val lastLoginDate: String = ""
)

fun SiswaEntity.toDomain(): Siswa {
    return Siswa(
        id = id,
        nim = nim,
        nama = nama,
        kelas = kelas,
        avatarResId = avatarResId,
        totalXp = totalXp,
        currentLevel = currentLevel,
        loginStreak = loginStreak,
        createdAt = createdAt,
        lastActiveAt = lastActiveAt,
        completedSubBabs = completedSubBabs,
        lastLoginDate = lastLoginDate
    )
}

fun Siswa.toEntity(password: String = ""): SiswaEntity {
    return SiswaEntity(
        id = id,
        nim = nim,
        password = password, // Password should be managed carefully
        nama = nama,
        kelas = kelas,
        avatarResId = avatarResId,
        totalXp = totalXp,
        currentLevel = currentLevel,
        loginStreak = loginStreak,
        createdAt = createdAt,
        lastActiveAt = lastActiveAt,
        completedSubBabs = completedSubBabs,
        lastLoginDate = lastLoginDate
    )
}
