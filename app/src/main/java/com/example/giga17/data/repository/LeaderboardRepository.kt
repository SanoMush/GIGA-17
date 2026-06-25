package com.example.giga17.data.repository

import com.example.giga17.data.model.Siswa
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LeaderboardRepository {

    fun getRealTimeLeaderboard(): Flow<List<Siswa>> = callbackFlow {
        val db = FirebaseFirestore.getInstance()

        val listener = db.collection("users")
            .orderBy("total_xp", Query.Direction.DESCENDING)
            .limit(20)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val siswaList = snapshot.documents.mapNotNull { doc ->
                        try {
                            val nim = doc.id.substringBefore("@")
                            val nama = doc.getString("nama") ?: "Siswa"
                            val kelas = doc.getString("kelas") ?: "X"
                            val totalXp = (doc.getLong("total_xp") ?: doc.getLong("totalXp"))?.toInt() ?: 0
                            val currentLevel = (doc.getLong("current_level") ?: doc.getLong("currentLevel"))?.toInt() ?: 1
                            val loginStreak = doc.getLong("login_streak")?.toInt() ?: 0
                            val avatarResId = doc.getString("avatarResId") ?: "avatar_default"
                            
                            // Map to Siswa domain model
                            // id is not present in Firestore, use nim.hashCode() for uniqueness in UI
                            Siswa(
                                id = nim.hashCode(),
                                nim = nim,
                                nama = nama,
                                kelas = kelas,
                                avatarResId = avatarResId,
                                totalXp = totalXp,
                                currentLevel = currentLevel,
                                loginStreak = loginStreak,
                                createdAt = 0L,
                                lastActiveAt = 0L,
                                completedSubBabs = emptyList(),
                                lastLoginDate = ""
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                    trySend(siswaList)
                }
            }

        awaitClose { 
            listener.remove() 
        }
    }
}
