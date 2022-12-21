package com.example.project_01_shopping_app.util.event

import com.example.project_01_shopping_app.presentation.main.MainActivity
import kotlinx.coroutines.flow.MutableSharedFlow

class MenuChangeEventBus {

    val mainTabMenuFlow = MutableSharedFlow<MainActivity.MainTabMenu>()

    suspend fun changeMenu(menu: MainActivity.MainTabMenu) {
        mainTabMenuFlow.emit(menu)
    }
}