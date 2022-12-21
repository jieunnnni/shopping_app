package com.example.project_01_shopping_app

import android.app.Application
import android.content.Context
import com.example.project_01_shopping_app.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ShoppingAppApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@ShoppingAppApplication)
            modules(appModule)
        }
    }

}