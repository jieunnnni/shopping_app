package com.example.project_01_shopping_app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.project_01_shopping_app.data.db.dao.ProductDao
import com.example.project_01_shopping_app.data.entity.ProductEntity
import com.example.project_01_shopping_app.util.converter.DateConverter

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class ProductDatabase: RoomDatabase() {

    companion object {
        const val DB_NAME = "ProductDataBase.db"
    }

    abstract fun productDao(): ProductDao
}