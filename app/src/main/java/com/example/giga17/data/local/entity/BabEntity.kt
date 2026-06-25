package com.example.giga17.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bab")
data class BabEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "judul_bab")
    val judulBab: String,

    @ColumnInfo(name = "kuis_akhir_bab_json")
    val kuisAkhirBabJson: String?,

    @ColumnInfo(name = "tipe_simulasi")
    val tipeSimulasi: String?,

    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true
)
