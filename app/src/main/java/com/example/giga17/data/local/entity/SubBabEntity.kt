package com.example.giga17.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "sub_bab",
    foreignKeys = [
        ForeignKey(
            entity = BabEntity::class,
            parentColumns = ["id"],
            childColumns = ["bab_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("bab_id")
    ]
)
data class SubBabEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "bab_id")
    val babId: Int,

    @ColumnInfo(name = "judul_sub_bab")
    val judulSubBab: String,

    @ColumnInfo(name = "isi_teks")
    val isiTeks: String,


    @ColumnInfo(name = "kuis_sub_bab_json")
    val kuisSubBabJson: String?
)
