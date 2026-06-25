package com.example.giga17.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.giga17.data.local.entity.SubBabEntity

@Dao
interface SubBabDao {
    @Query("SELECT * FROM sub_bab ORDER BY id ASC")
    suspend fun getAllSubBab(): List<SubBabEntity>

    @Query("SELECT * FROM sub_bab WHERE bab_id = :babId ORDER BY id ASC")
    suspend fun getSubBabByBabId(babId: Int): List<SubBabEntity>

    @Query("SELECT * FROM sub_bab WHERE id = :id LIMIT 1")
    suspend fun getSubBabById(id: Int): SubBabEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubBab(subBab: SubBabEntity): Long
}
