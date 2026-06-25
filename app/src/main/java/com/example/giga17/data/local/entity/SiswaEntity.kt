package com.example.giga17.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "siswa",
    indices = [Index(value = ["nim"], unique = true)]
)
data class SiswaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "nim")
    val nim: String,

    @ColumnInfo(name = "password")
    val password: String,


    @ColumnInfo(name = "nama")
    val nama: String,

    @ColumnInfo(name = "kelas")
    val kelas: String,

    @ColumnInfo(name = "avatar_res_id")
    val avatarResId: String = "avatar_default",

    @ColumnInfo(name = "total_xp")
    val totalXp: Int = 0,

    @ColumnInfo(name = "current_level")
    val currentLevel: Int = 1,

    @ColumnInfo(name = "login_streak")
    val loginStreak: Int = 0,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "last_active_at")
    val lastActiveAt: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "completed_sub_babs")
    val completedSubBabs: List<String> = emptyList(),

    @ColumnInfo(name = "last_login_date")
    val lastLoginDate: String = ""
)
