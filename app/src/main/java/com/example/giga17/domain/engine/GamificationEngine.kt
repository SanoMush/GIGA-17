package com.example.giga17.domain.engine

import com.example.giga17.data.model.Pencapaian
import com.example.giga17.data.model.Progres
import com.example.giga17.data.model.Siswa

/**
 * GamificationEngine
 * Mengatur semua logika murni (pure functions) terkait kalkulasi XP, Level, dan Evaluasi Badge.
 */
object GamificationEngine {

    // Thresholds XP per level (Level 1: 0, Level 2: 100, dst)
    val levelThresholds = listOf(0, 100, 300, 600, 1000, 1500, 2100, 2800, 3600, 4500)

    /**
     * a. Fungsi kalkulasi XP
     * Dihitung berdasarkan skor dan attempt (jumlah percobaan).
     */
    fun calculateXp(skor: Int, attempt: Int, baseXp: Int = 10): Int {
        if (skor < 60) return 0 // Skor minimum untuk mendapatkan XP

        var earnedXp = baseXp + (skor / 10)

        // Bonus jika berhasil mendapat skor bagus di percobaan pertama
        if (attempt == 1 && skor >= 80) {
            earnedXp += 5
        }

        return earnedXp
    }

    /**
     * b. Fungsi pengecekan Level
     * Mengembalikan current level berdasarkan total XP yang dikumpulkan.
     */
    fun calculateLevel(totalXp: Int): Int {
        for (i in levelThresholds.indices.reversed()) {
            if (totalXp >= levelThresholds[i]) {
                return i + 1 // Level dimulai dari 1
            }
        }
        return 1
    }

    // Badge Definitions
    data class BadgeDef(
        val id: String,
        val name: String,
        val description: String,
        val category: String, // "LEVEL", "STREAK", "MATERI", "KUIS"
        val icon: String // "Star", "Face", "ThumbUp", "Build"
    )

    val ALL_AVAILABLE_BADGES = listOf(
        // Kategori 1: Milestones Level
        BadgeDef("badge_level_5", "Novice Coder", "Mencapai Level 5 untuk pertama kalinya.", "LEVEL", "Star"),
        BadgeDef("badge_level_10", "Logic Apprentice", "Berhasil menembus dua digit, mencapai Level 10.", "LEVEL", "Star"),
        BadgeDef("badge_level_25", "Syntax Knight", "Menunjukkan dedikasi tinggi dengan mencapai Level 25.", "LEVEL", "Star"),
        BadgeDef("badge_level_50", "Code Master", "Separuh jalan menuju puncak, mencapai Level 50.", "LEVEL", "Star"),
        BadgeDef("badge_level_100", "Supreme Programmer", "Mencapai level maksimal (Level 100). Sang Legenda!", "LEVEL", "Star"),

        // Kategori 2: Login Streaks
        BadgeDef("badge_streak_3", "Warming Up", "Login dan belajar selama 3 hari berturut-turut.", "STREAK", "Face"),
        BadgeDef("badge_streak_7", "Weekly Dedication", "Mempertahankan streak login selama 1 minggu penuh.", "STREAK", "Face"),
        BadgeDef("badge_streak_14", "Unstoppable Habit", "Login tanpa bolong selama 2 minggu.", "STREAK", "Face"),
        BadgeDef("badge_streak_21", "Coding Addict", "Mengubah coding menjadi kebiasaan kuat selama 21 hari.", "STREAK", "Face"),
        BadgeDef("badge_streak_30", "Monthly Legend", "Dedikasi absolut! Login 30 hari berturut-turut tanpa putus.", "STREAK", "Face"),

        // Kategori 3: Eksplorasi Materi & Interaksi
        BadgeDef("badge_materi_1", "First Step", "Menyelesaikan bacaan Sub-bab pertama.", "MATERI", "Info"),
        BadgeDef("badge_materi_5", "Knowledge Seeker", "Menyelesaikan 5 Sub-bab materi.", "MATERI", "Info"),
        BadgeDef("badge_materi_all", "The Scholar", "Menyelesaikan seluruh materi bacaan di dalam aplikasi.", "MATERI", "Info"),
        BadgeDef("badge_simulasi_1", "Curious Mind", "Berhasil memicu dan mencoba 1 Simulasi Interaktif.", "MATERI", "ThumbUp"),
        BadgeDef("badge_simulasi_all", "The Explorer", "Mencoba semua Interactive Playground yang ada di dalam aplikasi.", "MATERI", "ThumbUp"),

        // Kategori 5: Kuis & Ujian (We call it Kategori 4 now visually, but let's keep ID logic)
        BadgeDef("badge_kuis_1", "The Initiate", "Menyelesaikan Kuis Sub-bab untuk pertama kalinya.", "KUIS", "Build"),
        BadgeDef("badge_kuis_100_1", "Flawless Victory", "Mendapatkan Nilai Sempurna (100) pada satu Kuis Sub-bab.", "KUIS", "Build"),
        BadgeDef("badge_kuis_100_5", "Quiz Champion", "Mendapatkan Nilai Sempurna (100) pada 5 kuis yang berbeda.", "KUIS", "Build"),
        BadgeDef("badge_kuis_bab_1", "Boss Challenger", "Berani menantang dan menyelesaikan 1 Kuis Akhir Bab.", "KUIS", "Build"),
        BadgeDef("badge_kuis_bab_all", "Conqueror of Logic", "Menyelesaikan semua Kuis Akhir Bab dengan nilai di atas KKM (70).", "KUIS", "Build")
    )

    fun evaluateBadges(
        siswa: Siswa,
        progresList: List<Progres>,
        logEvents: List<com.example.giga17.data.local.entity.LogEventEntity>,
        existingBadges: List<Pencapaian>
    ): List<Pencapaian> {
        val newBadges = mutableListOf<Pencapaian>()
        val existingIds = existingBadges.map { it.badgeId }.toSet()

        fun checkAndAdd(badgeId: String, condition: Boolean) {
            if (condition && !existingIds.contains(badgeId)) {
                val def = ALL_AVAILABLE_BADGES.find { it.id == badgeId } ?: return
                newBadges.add(
                    Pencapaian(
                        id = 0,
                        siswaId = siswa.id,
                        badgeId = def.id,
                        badgeName = def.name,
                        badgeDescription = def.description,
                        badgeCategory = def.category,
                        badgeIcon = def.icon,
                        unlockedAt = System.currentTimeMillis()
                    )
                )
            }
        }

        // Kategori 1: Level
        val level = siswa.currentLevel
        checkAndAdd("badge_level_5", level >= 5)
        checkAndAdd("badge_level_10", level >= 10)
        checkAndAdd("badge_level_25", level >= 25)
        checkAndAdd("badge_level_50", level >= 50)
        checkAndAdd("badge_level_100", level >= 100)

        // Kategori 2: Streak
        val streak = siswa.loginStreak
        checkAndAdd("badge_streak_3", streak >= 3)
        checkAndAdd("badge_streak_7", streak >= 7)
        checkAndAdd("badge_streak_14", streak >= 14)
        checkAndAdd("badge_streak_21", streak >= 21)
        checkAndAdd("badge_streak_30", streak >= 30)

        // Kategori 3: Materi & Simulasi
        val completedSubbabs = progresList.count { it.subBabId != null && it.status == "SELESAI" }
        checkAndAdd("badge_materi_1", completedSubbabs >= 1)
        checkAndAdd("badge_materi_5", completedSubbabs >= 5)
        // Asumsikan total subbab materi ada 15 (akan diverifikasi ulang di app)
        checkAndAdd("badge_materi_all", completedSubbabs >= 15) 

        val simulasiTried = logEvents.filter { it.eventType.startsWith("SIMULASI_TRIED_") }.map { it.eventType }.distinct().count()
        checkAndAdd("badge_simulasi_1", simulasiTried >= 1)
        checkAndAdd("badge_simulasi_all", simulasiTried >= 4) // Asumsi ada 4 simulasi

        // Kategori 4 (Kuis)
        val kuisSubbab = progresList.filter { it.subBabId != null && it.status == "SELESAI" && it.skor > 0 }
        checkAndAdd("badge_kuis_1", kuisSubbab.isNotEmpty())
        
        val perfectKuis = kuisSubbab.count { it.skor == 100 }
        checkAndAdd("badge_kuis_100_1", perfectKuis >= 1)
        checkAndAdd("badge_kuis_100_5", perfectKuis >= 5)

        val kuisAkhirBab = progresList.filter { it.babId != null && it.status == "SELESAI" }
        checkAndAdd("badge_kuis_bab_1", kuisAkhirBab.isNotEmpty())
        
        // Asumsi ada 4 bab
        val passedAkhirBab = kuisAkhirBab.count { it.skor >= 70 }
        checkAndAdd("badge_kuis_bab_all", passedAkhirBab >= 4)

        return newBadges
    }
}
