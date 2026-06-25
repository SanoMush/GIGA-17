package com.example.giga17.data.model

import com.example.giga17.data.local.entity.ProgresEntity

data class Progres(
    val id: Int,
    val siswaId: Int,
    val subBabId: Int? = null,
    val babId: Int? = null,
    val skor: Int,
    val waktuPengerjaanDetik: Int,
    val status: String,
    val attempt: Int,
    val xpEarned: Int,
    val completedAt: Long?,
    val lastAccessedAt: Long
)

fun ProgresEntity.toDomain(): Progres {
    return Progres(
        id = id,
        siswaId = siswaId,
        subBabId = subBabId,
        babId = babId,
        skor = skor,
        waktuPengerjaanDetik = waktuPengerjaanDetik,
        status = status,
        attempt = attempt,
        xpEarned = xpEarned,
        completedAt = completedAt,
        lastAccessedAt = lastAccessedAt
    )
}

fun Progres.toEntity(): ProgresEntity {
    return ProgresEntity(
        id = id,
        siswaId = siswaId,
        subBabId = subBabId,
        babId = babId,
        skor = skor,
        waktuPengerjaanDetik = waktuPengerjaanDetik,
        status = status,
        attempt = attempt,
        xpEarned = xpEarned,
        completedAt = completedAt,
        lastAccessedAt = lastAccessedAt
    )
}
