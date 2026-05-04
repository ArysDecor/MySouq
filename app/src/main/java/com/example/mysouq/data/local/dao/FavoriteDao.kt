package com.example.mysouq.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mysouq.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT productId FROM favorites")
    fun observeFavoriteIds(): Flow<List<Int>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE productId = :productId)")
    suspend fun isFavorite(productId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(fav: FavoriteEntity): Long

    @Query("DELETE FROM favorites WHERE productId = :productId")
    suspend fun remove(productId: Int): Int
}
