package com.example.giga17.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.giga17.data.local.entity.SiswaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SiswaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSiswa(siswa: SiswaEntity): Long

    @Update
    suspend fun updateSiswa(siswa: SiswaEntity): Int

    @Delete
    suspend fun deleteSiswa(siswa: SiswaEntity): Int

    @Query("SELECT * FROM siswa WHERE id = :id")
    suspend fun getSiswaById(id: Int): SiswaEntity?

    @Query("SELECT * FROM siswa WHERE id = :id")
    fun observeSiswaById(id: Int): Flow<SiswaEntity?>

    @Query("SELECT * FROM siswa WHERE nim = :nim")
    suspend fun getSiswaByNim(nim: String): SiswaEntity?

    @Query("SELECT * FROM siswa WHERE nim = :nim AND password = :password LIMIT 1")
    suspend fun authenticateLogin(nim: String, password: String): SiswaEntity?

    @Query("SELECT * FROM siswa ORDER BY total_xp DESC")
    fun getAllSiswaByXp(): Flow<List<SiswaEntity>>
}
