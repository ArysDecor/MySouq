package com.example.mysouq.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mysouq.data.local.dao.CartDao
import com.example.mysouq.data.local.dao.FavoriteDao
import com.example.mysouq.data.local.dao.ProductDao
import com.example.mysouq.data.local.entity.CartItemEntity
import com.example.mysouq.data.local.entity.FavoriteEntity
import com.example.mysouq.data.local.entity.ProductEntity

@Database(
    entities = [
        ProductEntity::class,
        CartItemEntity::class,
        FavoriteEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        const val NAME = "mysouq.db"
    }
}
