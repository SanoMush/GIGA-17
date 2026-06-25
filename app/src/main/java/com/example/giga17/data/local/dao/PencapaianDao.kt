package com.example.giga17.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.giga17.data.local.entity.PencapaianEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PencapaianDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPencapaian(pencapaian: PencapaianEntity): Long

    @Delete
    suspend fun deletePencapaian(pencapaian: PencapaianEntity): Int

    @Query("SELECT * FROM pencapaian WHERE siswa_id = :siswaId")
    fun getPencapaianBySiswaId(siswaId: Int): Flow<List<PencapaianEntity>>

    @Query("SELECT * FROM pencapaian WHERE siswa_id = :siswaId")
    suspend fun getPencapaianListBySiswaId(siswaId: Int): List<PencapaianEntity>

    @Query("SELECT * FROM pencapaian WHERE siswa_id = :siswaId AND badge_id = :badgeId LIMIT 1")
    suspend fun getPencapaianBySiswaAndBadge(siswaId: Int, badgeId: String): PencapaianEntity?
}
