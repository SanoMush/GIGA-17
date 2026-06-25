package com.example.giga17.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.giga17.data.local.entity.ProgresEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgresDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgres(progres: ProgresEntity): Long

    @Update
    suspend fun updateProgres(progres: ProgresEntity): Int

    @Delete
    suspend fun deleteProgres(progres: ProgresEntity): Int

    @Query("SELECT * FROM progres WHERE siswa_id = :siswaId")
    fun getProgresBySiswaId(siswaId: Int): Flow<List<ProgresEntity>>

    @Query("SELECT * FROM progres WHERE siswa_id = :siswaId")
    suspend fun getProgresListBySiswaId(siswaId: Int): List<ProgresEntity>

    @Query("SELECT * FROM progres WHERE siswa_id = :siswaId AND sub_bab_id = :subBabId LIMIT 1")
    suspend fun getProgresBySiswaAndSubBab(siswaId: Int, subBabId: Int): ProgresEntity?

    @Query("SELECT * FROM progres WHERE siswa_id = :siswaId AND bab_id = :babId LIMIT 1")
    suspend fun getProgresBySiswaAndBab(siswaId: Int, babId: Int): ProgresEntity?
}
