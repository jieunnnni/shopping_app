package com.example.project_01_shopping_app.data.db

import android.content.Context
import androidx.room.Room

fun provideDB(context: Context): ProductDatabase =
    Room.databaseBuilder(context, ProductDatabase::class.java, ProductDatabase.DB_NAME).build()

fun provideProductDao(database: ProductDatabase) = database.productDao()
