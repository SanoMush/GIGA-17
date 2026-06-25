package com.example.giga17.data.repository

import com.example.giga17.data.local.dao.ProgresDao
import com.example.giga17.data.local.Giga17Database
import com.example.giga17.data.local.entity.ProgresEntity
import com.example.giga17.data.model.Bab
import com.example.giga17.data.model.Progres
import com.example.giga17.data.model.toDomain
import com.example.giga17.data.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.room.withTransaction

class ProgresRepository(
    private val progresDao: ProgresDao,
    private val database: Giga17Database? = null
) {

    fun getProgresBySiswaId(siswaId: Int): Flow<List<Progres>> {
        return progresDao.getProgresBySiswaId(siswaId).map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun getProgresBySiswaAndSubBab(siswaId: Int, subBabId: Int): Progres? {
        return progresDao.getProgresBySiswaAndSubBab(siswaId, subBabId)?.toDomain()
    }

    suspend fun getProgresBySiswaAndBab(siswaId: Int, babId: Int): Progres? {
        return progresDao.getProgresBySiswaAndBab(siswaId, babId)?.toDomain()
    }

    suspend fun saveProgres(progres: Progres) {
        if (progres.id == 0) {
            progresDao.insertProgres(progres.toEntity())
        } else {
            progresDao.updateProgres(progres.toEntity())
        }
    }

    suspend fun getProgresListBySiswaIdSync(siswaId: Int): List<Progres> {
        return progresDao.getProgresListBySiswaId(siswaId).map { it.toDomain() }
    }

    suspend fun repairProgressWithTransaction(siswaId: Int, completedSubBabIds: List<String>, babList: List<Bab>) {
        if (database != null) {
            database.withTransaction {
                performRepair(siswaId, completedSubBabIds, babList)
            }
        } else {
            performRepair(siswaId, completedSubBabIds, babList)
        }
    }

    private suspend fun performRepair(siswaId: Int, completedSubBabIds: List<String>, babList: List<Bab>) {
        for (bab in babList) {
            for (subBab in bab.listSubBab) {
                if (completedSubBabIds.contains(subBab.id.toString())) {
                    val existing = progresDao.getProgresBySiswaAndSubBab(siswaId, subBab.id)
                    if (existing == null) {
                        progresDao.insertProgres(
                            ProgresEntity(
                                siswaId = siswaId,
                                subBabId = subBab.id,
                                babId = bab.id,
                                status = "SELESAI",
                                xpEarned = 10
                            )
                        )
                    } else if (existing.status != "SELESAI") {
                        progresDao.updateProgres(existing.copy(status = "SELESAI"))
                    }
                }
            }
        }
    }
}
