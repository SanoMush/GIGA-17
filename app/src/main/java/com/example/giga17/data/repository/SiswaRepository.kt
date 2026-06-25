package com.example.giga17.data.repository

import com.example.giga17.data.local.dao.SiswaDao
import com.example.giga17.data.model.Siswa
import com.example.giga17.data.model.toDomain
import com.example.giga17.data.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FirebaseFirestore
import com.example.giga17.domain.engine.GamificationEngine
import com.example.giga17.domain.engine.GamificationEvent
import com.example.giga17.domain.engine.GamificationEventBus

class SiswaRepository(
    private val siswaDao: SiswaDao,
    private val pencapaianRepository: PencapaianRepository? = null,
    private val gamificationEventBus: GamificationEventBus? = null
) {

    var currentSiswaId: Int? = null

    suspend fun login(nim: String, password: String): Siswa? {
        val siswa = siswaDao.authenticateLogin(nim, password)?.toDomain()
        if (siswa != null) {
            currentSiswaId = siswa.id
            checkAndUpdateLoginStreak(siswa.id)
        }
        return siswa
    }

    suspend fun syncSiswaProfileFromFirebase(nim: String): Siswa {
        val docId = "${nim}@sma17.edu"
        val document = FirebaseFirestore.getInstance()
            .collection("users")
            .document(docId)
            .get()
            .await()

        if (!document.exists()) {
            throw Exception("Dokumen '$docId' tidak ditemukan di Firestore")
        }
                val nama = document.getString("nama") ?: "Siswa"
                val kelas = document.getString("kelas") ?: "X"
                // Support both snake_case and camelCase
                val totalXp = (document.getLong("total_xp") ?: document.getLong("totalXp"))?.toInt() ?: 0
                val currentLevel = (document.getLong("current_level") ?: document.getLong("currentLevel"))?.toInt() ?: 1
                
                val completedSubBabsAny = document.get("completed_sub_babs") as? List<*>
                val completedSubBabsList = completedSubBabsAny?.mapNotNull { it?.toString() } ?: emptyList()
                val firestoreLoginStreak = document.getLong("login_streak")?.toInt() ?: 0
                val firestoreLastLoginDate = document.getString("last_login_date") ?: ""

                var siswaEntity = siswaDao.getSiswaByNim(nim)
                if (siswaEntity == null) {
                    val newSiswa = com.example.giga17.data.local.entity.SiswaEntity(
                        id = 0,
                        nim = nim,
                        password = "firebase_auth",
                        nama = nama,
                        kelas = kelas,
                        avatarResId = "avatar_default",
                        totalXp = totalXp,
                        currentLevel = currentLevel,
                        loginStreak = firestoreLoginStreak,
                        createdAt = System.currentTimeMillis(),
                        lastActiveAt = System.currentTimeMillis(),
                        completedSubBabs = completedSubBabsList,
                        lastLoginDate = firestoreLastLoginDate
                    )
                    val insertedId = siswaDao.insertSiswa(newSiswa)
                    currentSiswaId = insertedId.toInt()
                    checkAndUpdateLoginStreak(currentSiswaId!!)
                    return siswaDao.getSiswaById(currentSiswaId!!)!!.toDomain()
                } else {
                    val updatedSiswa = siswaEntity.copy(
                        nama = nama,
                        kelas = kelas,
                        totalXp = totalXp,
                        currentLevel = currentLevel,
                        loginStreak = firestoreLoginStreak,
                        lastActiveAt = System.currentTimeMillis(),
                        completedSubBabs = completedSubBabsList,
                        lastLoginDate = firestoreLastLoginDate
                    )
                    siswaDao.updateSiswa(updatedSiswa)
                    currentSiswaId = updatedSiswa.id
                    checkAndUpdateLoginStreak(updatedSiswa.id)
                    return siswaDao.getSiswaById(updatedSiswa.id)!!.toDomain()
                }
    }

    suspend fun getSiswaData(id: Int): Siswa? {
        return siswaDao.getSiswaById(id)?.toDomain()
    }

    fun getAllSiswaByXp(): Flow<List<Siswa>> {
        return siswaDao.getAllSiswaByXp().map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun updateSiswa(siswa: Siswa) {
        siswaDao.updateSiswa(siswa.toEntity())
    }

    suspend fun updateAvatar(siswaId: Int, avatarName: String) {
        val siswaEntity = siswaDao.getSiswaById(siswaId) ?: return
        val updated = siswaEntity.copy(avatarResId = avatarName)
        siswaDao.updateSiswa(updated)
        
        try {
            val docId = "${siswaEntity.nim}@sma17.edu"
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(docId)
                .update("avatarResId", avatarName)
                .await()
        } catch (e: Exception) {
            // Error updating firestore, but local is updated
        }
    }

    fun observeCurrentSiswa(): Flow<Siswa?> {
        return currentSiswaId?.let { id ->
            siswaDao.observeSiswaById(id).map { it?.toDomain() }
        } ?: flowOf(null)
    }

    suspend fun addXp(amount: Int) {
        val id = currentSiswaId ?: return
        val siswaEntity = siswaDao.getSiswaById(id) ?: return
        
        val newXp = siswaEntity.totalXp + amount
        
        // Leveling logic using GamificationEngine
        val newLevel = GamificationEngine.calculateLevel(newXp)
        
        val updatedSiswa = siswaEntity.copy(
            totalXp = newXp,
            currentLevel = newLevel
        )
        siswaDao.updateSiswa(updatedSiswa)
        
        if (newLevel > siswaEntity.currentLevel) {
            gamificationEventBus?.sendEvent(GamificationEvent.LevelUp(newLevel))
        }
        
        // Update Firestore silently
        try {
            val docId = "${siswaEntity.nim}@sma17.edu"
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(docId)
                .update(
                    mapOf(
                        "total_xp" to newXp,
                        "current_level" to newLevel
                    )
                )
        } catch (e: Exception) {
            // Error updating firestore, but local is updated
        }
        
        pencapaianRepository?.checkAndUnlockBadges(id)
    }

    suspend fun markSubBabComplete(subBabId: String) {
        val id = currentSiswaId ?: return
        val siswaEntity = siswaDao.getSiswaById(id) ?: return
        
        if (siswaEntity.completedSubBabs.contains(subBabId)) return
        
        val newCompleted = siswaEntity.completedSubBabs + subBabId
        val updatedSiswa = siswaEntity.copy(completedSubBabs = newCompleted)
        siswaDao.updateSiswa(updatedSiswa)
        
        try {
            val docId = "${siswaEntity.nim}@sma17.edu"
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(docId)
                .update("completed_sub_babs", newCompleted)
        } catch (e: Exception) {
            // Error updating firestore, but local is updated
        }
    }

    suspend fun checkAndUpdateLoginStreak(siswaId: Int) {
        val siswaEntity = siswaDao.getSiswaById(siswaId) ?: return
        
        val todayStr = java.time.LocalDate.now().toString()
        val lastDateStr = siswaEntity.lastLoginDate
        
        var newStreak = siswaEntity.loginStreak
        var isUpdated = false

        if (lastDateStr.isEmpty()) {
            newStreak = 1
            isUpdated = true
        } else if (lastDateStr != todayStr) {
            try {
                val lastDate = java.time.LocalDate.parse(lastDateStr)
                val today = java.time.LocalDate.parse(todayStr)
                val daysBetween = java.time.temporal.ChronoUnit.DAYS.between(lastDate, today)

                if (daysBetween == 1L) {
                    newStreak += 1
                    isUpdated = true
                } else if (daysBetween > 1L) {
                    newStreak = 1
                    isUpdated = true
                }
            } catch (e: Exception) {
                newStreak = 1
                isUpdated = true
            }
        }
        
        if (isUpdated || lastDateStr != todayStr) {
            val updatedSiswa = siswaEntity.copy(
                loginStreak = newStreak,
                lastLoginDate = todayStr
            )
            siswaDao.updateSiswa(updatedSiswa)
            
            try {
                val docId = "${siswaEntity.nim}@sma17.edu"
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(docId)
                    .update(
                        mapOf(
                            "login_streak" to newStreak,
                            "last_login_date" to todayStr
                        )
                    ).await()
            } catch (e: Exception) {
                // Ignore silent update error
            }
        }
        
        pencapaianRepository?.checkAndUnlockBadges(siswaId)
    }
}
