package com.example.giga17.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity LogEvent — Menyimpan event log untuk keperluan observasi eksperimen.
 *
 * Digunakan oleh ObservationManager sebagai persistent storage untuk
 * mencatat metrik reliability dan efficiency selama fase kuasi-eksperimen.
 *
 * ### Tipe Event yang Direkam:
 *
 * | eventType         | Deskripsi                              | Layer        |
 * |-------------------|----------------------------------------|--------------|
 * | SCREEN_LOAD       | Waktu loading screen                   | UI           |
 * | USER_ACTION       | Interaksi user (klik, navigasi)        | UI           |
 * | STATE_TRANSITION  | Perpindahan state di ViewModel         | ViewModel    |
 * | ENGINE_CALC       | Waktu kalkulasi gamification engine    | Engine       |
 * | DB_QUERY          | Durasi query database                  | Repository   |
 * | DB_WRITE          | Durasi operasi write database          | Repository   |
 * | ERROR             | Error/exception yang terjadi           | Semua layer  |
 * | SESSION           | Start/end sesi penggunaan              | Application  |
 *
 * [siswaId] bersifat nullable untuk mengakomodasi event level
 * aplikasi yang terjadi sebelum login (e.g. app startup).
 */
@Entity(
    tableName = "log_event",
    foreignKeys = [
        ForeignKey(
            entity = SiswaEntity::class,
            parentColumns = ["id"],
            childColumns = ["siswa_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("siswa_id"),
        Index("event_type"),
        Index("timestamp")
    ]
)
data class LogEventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /** ID siswa terkait — null untuk event level aplikasi (pre-login) */
    @ColumnInfo(name = "siswa_id")
    val siswaId: Int? = null,

    /**
     * Tipe event yang direkam.
     * Lihat tabel di dokumentasi class untuk daftar lengkap.
     */
    @ColumnInfo(name = "event_type")
    val eventType: String,

    /** Detail spesifik event (nama screen, nama query, pesan error, dll.) */
    @ColumnInfo(name = "event_detail")
    val eventDetail: String,

    /** Durasi event dalam milidetik — null untuk event non-timed */
    @ColumnInfo(name = "duration_ms")
    val durationMs: Long? = null,

    /** Apakah event berhasil dieksekusi */
    @ColumnInfo(name = "is_success")
    val isSuccess: Boolean = true,

    /** Data tambahan dalam format JSON string (opsional) */
    @ColumnInfo(name = "extra_data")
    val extraData: String? = null,

    /** Timestamp pencatatan event (epoch millis) */
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis()
)
