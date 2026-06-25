package com.example.giga17.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.giga17.data.local.converter.TypeConverters as AppTypeConverters
import com.example.giga17.data.local.dao.LogEventDao
import com.example.giga17.data.local.dao.BabDao
import com.example.giga17.data.local.dao.SubBabDao
import com.example.giga17.data.local.dao.PencapaianDao
import com.example.giga17.data.local.dao.ProgresDao
import com.example.giga17.data.local.dao.SiswaDao
import com.example.giga17.data.local.entity.LogEventEntity
import com.example.giga17.data.local.entity.BabEntity
import com.example.giga17.data.local.entity.SubBabEntity
import com.example.giga17.data.local.entity.PencapaianEntity
import com.example.giga17.data.local.entity.ProgresEntity
import com.example.giga17.data.local.entity.SiswaEntity

@Database(
    entities = [
        SiswaEntity::class,
        ProgresEntity::class,
        PencapaianEntity::class,
        BabEntity::class,
        SubBabEntity::class,
        LogEventEntity::class
    ],
    version = 9,
    exportSchema = false
)
@TypeConverters(AppTypeConverters::class)
abstract class Giga17Database : RoomDatabase() {
    abstract fun siswaDao(): SiswaDao
    abstract fun progresDao(): ProgresDao
    abstract fun pencapaianDao(): PencapaianDao
    abstract fun babDao(): BabDao
    abstract fun subBabDao(): SubBabDao
    abstract fun logEventDao(): LogEventDao
}
