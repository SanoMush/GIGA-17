package com.example.giga17.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.giga17.data.local.entity.BabEntity

@Dao
interface BabDao {
    @Query("SELECT * FROM bab ORDER BY id ASC")
    suspend fun getAllBab(): List<BabEntity>

    @Query("SELECT * FROM bab WHERE id = :id LIMIT 1")
    suspend fun getBabById(id: Int): BabEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBab(bab: BabEntity): Long
}
