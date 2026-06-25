package com.example.giga17.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.giga17.data.local.entity.LogEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LogEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogEvent(logEvent: LogEventEntity): Long

    @Query("SELECT * FROM log_event ORDER BY timestamp DESC")
    fun getAllLogEvents(): Flow<List<LogEventEntity>>

    @Query("SELECT * FROM log_event WHERE siswa_id = :siswaId ORDER BY timestamp DESC")
    fun getLogEventsBySiswaId(siswaId: Int): Flow<List<LogEventEntity>>

    @Query("SELECT * FROM log_event WHERE siswa_id = :siswaId")
    suspend fun getLogEventsListBySiswaId(siswaId: Int): List<LogEventEntity>

    @Query("DELETE FROM log_event")
    suspend fun clearAllLogs(): Int
}
