package com.example.giga17

import android.app.Application
import com.example.giga17.di.AppContainer
import com.example.giga17.di.DefaultAppContainer

class Giga17Application : Application() {
    
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
