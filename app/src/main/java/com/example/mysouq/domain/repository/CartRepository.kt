package com.example.mysouq.domain.repository

import com.example.mysouq.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun observeCart(): Flow<List<CartItem>>
    fun observeItemCount(): Flow<Int>
    fun observeTotal(): Flow<Double>
    suspend fun addToCart(productId: Int, quantity: Int = 1)
    suspend fun updateQuantity(productId: Int, quantity: Int)
    suspend fun removeFromCart(productId: Int)
    suspend fun clearCart()
}
