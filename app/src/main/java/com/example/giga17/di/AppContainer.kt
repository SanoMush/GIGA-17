package com.example.giga17.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.giga17.data.local.Giga17Database
import com.example.giga17.data.repository.BabRepository
import com.example.giga17.data.repository.ObservationRepository
import com.example.giga17.data.repository.PencapaianRepository
import com.example.giga17.data.repository.ProgresRepository
import com.example.giga17.data.repository.SiswaRepository
import com.example.giga17.data.repository.CompilerRepository
import com.example.giga17.domain.engine.GamificationEventBus

    interface AppContainer {
        val siswaRepository: SiswaRepository
        val progresRepository: ProgresRepository
        val pencapaianRepository: PencapaianRepository
        val babRepository: BabRepository
        val observationRepository: ObservationRepository
        val compilerRepository: CompilerRepository
        val gamificationEventBus: GamificationEventBus
    }
    
    class DefaultAppContainer(private val context: Context) : AppContainer {
    
        private val database: Giga17Database by lazy {
            Room.databaseBuilder(
                context,
                Giga17Database::class.java,
                "giga17_database"
            )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // Use Database Seeder
                    com.example.giga17.data.local.DatabaseSeeder.populateData(db)
                }

                override fun onDestructiveMigration(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                    super.onDestructiveMigration(db)
                    com.example.giga17.data.local.DatabaseSeeder.populateData(db)
                }
            })
            .fallbackToDestructiveMigration()
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()
        }
    
        override val gamificationEventBus: GamificationEventBus by lazy {
            GamificationEventBus()
        }

        override val siswaRepository: SiswaRepository by lazy {
            SiswaRepository(database.siswaDao(), pencapaianRepository, gamificationEventBus)
        }
    
        override val progresRepository: ProgresRepository by lazy {
        ProgresRepository(database.progresDao(), database)
    }
    
        override val pencapaianRepository: PencapaianRepository by lazy {
            PencapaianRepository(
                database.pencapaianDao(),
                database.siswaDao(),
                database.progresDao(),
                database.logEventDao(),
                gamificationEventBus
            )
        }
    
        override val babRepository: BabRepository by lazy {
            BabRepository(database.babDao(), database.subBabDao())
        }
    
        override val observationRepository: ObservationRepository by lazy {
            ObservationRepository(database.logEventDao())
        }
        
        override val compilerRepository: CompilerRepository by lazy {
            CompilerRepository()
        }
    }
