package com.example.giga17.data.model

import com.example.giga17.data.local.entity.PencapaianEntity

data class Pencapaian(
    val id: Int,
    val siswaId: Int,
    val badgeId: String,
    val badgeName: String,
    val badgeDescription: String,
    val badgeCategory: String,
    val badgeIcon: String,
    val unlockedAt: Long
)

fun PencapaianEntity.toDomain(): Pencapaian {
    return Pencapaian(
        id = id,
        siswaId = siswaId,
        badgeId = badgeId,
        badgeName = badgeName,
        badgeDescription = badgeDescription,
        badgeCategory = badgeCategory,
        badgeIcon = badgeIcon,
        unlockedAt = unlockedAt
    )
}

fun Pencapaian.toEntity(): PencapaianEntity {
    return PencapaianEntity(
        id = id,
        siswaId = siswaId,
        badgeId = badgeId,
        badgeName = badgeName,
        badgeDescription = badgeDescription,
        badgeCategory = badgeCategory,
        badgeIcon = badgeIcon,
        unlockedAt = unlockedAt
    )
}
