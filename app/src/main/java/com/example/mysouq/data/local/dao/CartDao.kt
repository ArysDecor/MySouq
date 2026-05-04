package com.example.mysouq.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mysouq.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

data class CartLine(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val region: String,
    val artisan: String,
    val imageUrl: String,
    val rating: Double,
    val reviewCount: Int,
    val isAvailable: Boolean,
    val quantity: Int
)

@Dao
interface CartDao {

    @Query(
        """
        SELECT p.id, p.name, p.description, p.price, p.category, p.region, p.artisan, p.imageUrl, p.rating, p.reviewCount, p.isAvailable, c.quantity
        FROM cart_items c
        INNER JOIN products p ON p.id = c.productId
        ORDER BY c.addedAt DESC
        """
    )
    fun observeCart(): Flow<List<CartLine>>

    @Query("SELECT COALESCE(SUM(quantity), 0) FROM cart_items")
    fun observeItemCount(): Flow<Int>

    @Query(
        """
        SELECT COALESCE(SUM(p.price * c.quantity), 0.0)
        FROM cart_items c
        INNER JOIN products p ON p.id = c.productId
        """
    )
    fun observeTotal(): Flow<Double>

    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    suspend fun findByProductId(productId: Int): CartItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: CartItemEntity): Long

    @Query("UPDATE cart_items SET quantity = :quantity WHERE productId = :productId")
    suspend fun updateQuantity(productId: Int, quantity: Int): Int

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun remove(productId: Int): Int

    @Query("DELETE FROM cart_items")
    suspend fun clear(): Int
}
