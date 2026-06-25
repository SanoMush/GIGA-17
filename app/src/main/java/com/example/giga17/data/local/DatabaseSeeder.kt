package com.example.giga17.data.local

import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseSeeder {
    fun populateData(db: SupportSQLiteDatabase) {
        // 1. Seed Siswa Dummy
        db.execSQL("""
            INSERT INTO siswa (nim, password, nama, kelas, avatar_res_id, total_xp, current_level, login_streak, created_at, last_active_at, completed_sub_babs, last_login_date)
            VALUES ('123456', '123', 'Siswa SMAN 17 Garut', 'X IPA 1', 'avatar_default', 150, 2, 0, 0, 0, '[]', '')
        """.trimIndent())

        // 2. Pencapaian Awal
        db.execSQL("""
            INSERT INTO pencapaian (siswa_id, badge_id, badge_name, badge_description, badge_category, badge_icon, unlocked_at)
            VALUES (1, 'first_login', 'Siswa Baru', 'Melakukan login pertama kali', 'MILESTONE', 'ic_badge_1', 0)
        """.trimIndent())

        // 3. Seed Data Materi dan Soal Kuis
        var babIdCounter = 1
        var subBabIdCounter = 1

        DataMateri.listMateri.forEach { bab ->
            val kuisAkhirJson = bab.kuisAkhirBab?.toJson()?.replace("'", "''") ?: ""
            val kuisStr = if (kuisAkhirJson.isEmpty()) "NULL" else "'${kuisAkhirJson}'"
            val tipeSimulasi = if (bab.tipeSimulasi == null) "NULL" else "'${bab.tipeSimulasi.replace("'", "''")}'"

            db.execSQL("""
                INSERT INTO bab (judul_bab, kuis_akhir_bab_json, tipe_simulasi, is_active)
                VALUES ('${bab.judulBab.replace("'", "''")}', $kuisStr, $tipeSimulasi, 1)
            """.trimIndent())

            val currentBabId = babIdCounter++

            bab.subBabs.forEach { subBab ->
                val kuisSubBabJson = subBab.kuis?.toJson()?.replace("'", "''") ?: ""
                val kuisSubBabStr = if (kuisSubBabJson.isEmpty()) "NULL" else "'${kuisSubBabJson}'"
                
                db.execSQL("""
                    INSERT INTO sub_bab (bab_id, judul_sub_bab, isi_teks, kuis_sub_bab_json)
                    VALUES ($currentBabId, '${subBab.judulSubBab.replace("'", "''")}', '${subBab.isiTeks.replace("'", "''")}', $kuisSubBabStr)
                """.trimIndent())
            }
        }
    }
}
