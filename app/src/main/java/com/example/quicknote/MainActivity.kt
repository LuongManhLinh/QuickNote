package com.example.quicknote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.quicknote.ui.mainscreen.MainScreen
import com.example.quicknote.ui.mainscreen.MainScreenViewModel
import com.example.quicknote.ui.theme.QuickNoteTheme

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainScreenViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            viewModel = ProjectViewModelProvider.provide(MainScreenViewModel::class)
            QuickNoteTheme {
                MainScreen(
                    viewModel = viewModel
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.deleteNotesInCache()
    }
}

