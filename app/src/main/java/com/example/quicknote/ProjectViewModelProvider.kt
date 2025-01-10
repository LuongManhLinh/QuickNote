package com.example.quicknote

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.quicknote.ui.mainscreen.MainScreenViewModel
import kotlin.reflect.KClass

object ProjectViewModelProvider {
    @Composable
    inline fun <reified T: ViewModel> provide(
        vmClass: KClass<T>
    ): T {
        val factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as NoteApplication
                val repo = application.noteContainer.noteRepository
                when (vmClass) {
                    MainScreenViewModel::class -> MainScreenViewModel(repo)
                    else -> throw IllegalArgumentException("Unknown ViewModel class: $vmClass")
                }
            }
        }

        return viewModel(factory = factory)
    }
}