package com.example.giga17.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


/**
 * Entity Pencapaian — Menyimpan badge/achievement yang telah diraih siswa.
 *
 * Setiap record merepresentasikan satu badge yang berhasil di-unlock.
 * Unique constraint pada [siswaId, badgeId] mencegah duplikasi badge
 * untuk siswa yang sama.
 *
 * Kategori badge:
 * - MILESTONE: Pencapaian pertama kali (e.g. "Selesaikan materi pertama")
 * - MASTERY: Penguasaan materi (e.g. "Skor ≥ 90 di bab X")
 * - STREAK: Konsistensi login (e.g. "Login 7 hari berturut")
 * - COMPLETIONIST: Menyelesaikan seluruh konten
 * - SPEED: Kecepatan pengerjaan
 * - PERFECTION: Skor sempurna
 */
@Entity(
    tableName = "pencapaian",
    foreignKeys = [
        ForeignKey(
            entity = SiswaEntity::class,
            parentColumns = ["id"],
            childColumns = ["siswa_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("siswa_id"),
        Index(value = ["siswa_id", "badge_id"], unique = true)
    ]
)
data class PencapaianEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /** ID siswa yang meraih pencapaian */
    @ColumnInfo(name = "siswa_id")
    val siswaId: Int,

    /** Identifier unik badge (e.g. "badge_first_adventure") */
    @ColumnInfo(name = "badge_id")
    val badgeId: String,

    /** Nama badge yang ditampilkan ke user */
    @ColumnInfo(name = "badge_name")
    val badgeName: String,

    /** Deskripsi cara memperoleh badge ini */
    @ColumnInfo(name = "badge_description")
    val badgeDescription: String,

    /**
     * Kategori badge:
     * MILESTONE | MASTERY | STREAK | COMPLETIONIST | SPEED | PERFECTION
     */
    @ColumnInfo(name = "badge_category")
    val badgeCategory: String,

    /** Nama resource icon badge (drawable resource name) */
    @ColumnInfo(name = "badge_icon")
    val badgeIcon: String = "ic_badge_default",

    /** Timestamp saat badge berhasil di-unlock (epoch millis) */
    @ColumnInfo(name = "unlocked_at")
    val unlockedAt: Long = System.currentTimeMillis()
)
