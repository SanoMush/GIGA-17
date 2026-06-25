package com.example.giga17.data.repository

import com.example.giga17.data.local.dao.LogEventDao
import com.example.giga17.data.local.entity.LogEventEntity
import kotlinx.coroutines.flow.Flow

class ObservationRepository(private val logEventDao: LogEventDao) {

    suspend fun logEvent(
        siswaId: Int?,
        eventType: String,
        eventDetail: String,
        durationMs: Long? = null,
        isSuccess: Boolean = true,
        extraData: String? = null
    ) {
        val log = LogEventEntity(
            siswaId = siswaId,
            eventType = eventType,
            eventDetail = eventDetail,
            durationMs = durationMs,
            isSuccess = isSuccess,
            extraData = extraData
        )
        logEventDao.insertLogEvent(log)
    }

    fun getAllLogEvents(): Flow<List<LogEventEntity>> {
        return logEventDao.getAllLogEvents()
    }

    fun getLogEventsBySiswaId(siswaId: Int): Flow<List<LogEventEntity>> {
        return logEventDao.getLogEventsBySiswaId(siswaId)
    }

    suspend fun clearAllLogs() {
        logEventDao.clearAllLogs()
    }
}
