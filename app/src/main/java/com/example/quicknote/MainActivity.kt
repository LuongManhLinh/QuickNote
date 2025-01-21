package com.example.quicknote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.quicknote.ui.mainscreen.MainScreen
import com.example.quicknote.ui.theme.QuickNoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuickNoteTheme {
                MainScreen()
            }
        }
    }
}

