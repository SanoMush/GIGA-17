package com.example.giga17.data.repository

import com.example.giga17.data.local.dao.PencapaianDao
import com.example.giga17.data.local.dao.SiswaDao
import com.example.giga17.data.local.dao.ProgresDao
import com.example.giga17.data.local.dao.LogEventDao
import com.example.giga17.data.model.Pencapaian
import com.example.giga17.data.model.toDomain
import com.example.giga17.data.model.toEntity
import com.example.giga17.domain.engine.GamificationEngine
import com.example.giga17.domain.engine.GamificationEvent
import com.example.giga17.domain.engine.GamificationEventBus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PencapaianRepository(
    private val pencapaianDao: PencapaianDao,
    private val siswaDao: SiswaDao,
    private val progresDao: ProgresDao,
    private val logEventDao: LogEventDao,
    private val gamificationEventBus: GamificationEventBus? = null
) {

    fun getPencapaianBySiswaId(siswaId: Int): Flow<List<Pencapaian>> {
        return pencapaianDao.getPencapaianBySiswaId(siswaId).map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun unlockPencapaian(pencapaian: Pencapaian) {
        val existing = pencapaianDao.getPencapaianBySiswaAndBadge(pencapaian.siswaId, pencapaian.badgeId)
        if (existing == null) {
            pencapaianDao.insertPencapaian(pencapaian.toEntity())
        }
    }

    suspend fun checkAndUnlockBadges(siswaId: Int) {
        val siswaEntity = siswaDao.getSiswaById(siswaId) ?: return
        val siswa = siswaEntity.toDomain()
        
        val progresList = progresDao.getProgresListBySiswaId(siswaId).map { it.toDomain() }
        val logEvents = logEventDao.getLogEventsListBySiswaId(siswaId)
        val existingBadges = pencapaianDao.getPencapaianListBySiswaId(siswaId).map { it.toDomain() }

        val newBadges = GamificationEngine.evaluateBadges(
            siswa = siswa,
            progresList = progresList,
            logEvents = logEvents,
            existingBadges = existingBadges
        )

        for (badge in newBadges) {
            val existing = pencapaianDao.getPencapaianBySiswaAndBadge(badge.siswaId, badge.badgeId)
            if (existing == null) {
                pencapaianDao.insertPencapaian(badge.toEntity())
                val badgeDef = GamificationEngine.ALL_AVAILABLE_BADGES.find { it.id == badge.badgeId }
                if (badgeDef != null) {
                    gamificationEventBus?.sendEvent(GamificationEvent.BadgeUnlocked(badgeDef))
                }
            }
        }
    }
}
