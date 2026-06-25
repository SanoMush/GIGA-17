package com.example.giga17

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import com.example.giga17.ui.theme.GIGA17Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val appContainer = (application as Giga17Application).container

        enableEdgeToEdge()
        setContent {
            GIGA17Theme {
                com.example.giga17.presentation.ui.navigation.MainScreen(appContainer = appContainer)
            }
        }
    }
}