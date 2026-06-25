package com.example.giga17.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "progres",
    foreignKeys = [
        ForeignKey(
            entity = SiswaEntity::class,
            parentColumns = ["id"],
            childColumns = ["siswa_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SubBabEntity::class,
            parentColumns = ["id"],
            childColumns = ["sub_bab_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BabEntity::class,
            parentColumns = ["id"],
            childColumns = ["bab_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("siswa_id"),
        Index("sub_bab_id"),
        Index("bab_id")
    ]
)
data class ProgresEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "siswa_id")
    val siswaId: Int,

    @ColumnInfo(name = "sub_bab_id")
    val subBabId: Int? = null,

    @ColumnInfo(name = "bab_id")
    val babId: Int? = null,

    @ColumnInfo(name = "skor")
    val skor: Int = 0,

    @ColumnInfo(name = "waktu_pengerjaan_detik")
    val waktuPengerjaanDetik: Int = 0,

    @ColumnInfo(name = "status")
    val status: String = "BELUM_MULAI",

    @ColumnInfo(name = "attempt")
    val attempt: Int = 1,

    @ColumnInfo(name = "xp_earned")
    val xpEarned: Int = 0,

    @ColumnInfo(name = "completed_at")
    val completedAt: Long? = null,

    @ColumnInfo(name = "last_accessed_at")
    val lastAccessedAt: Long = System.currentTimeMillis()
)
