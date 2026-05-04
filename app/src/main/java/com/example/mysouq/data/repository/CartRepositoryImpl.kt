package com.example.mysouq.data.repository

import com.example.mysouq.data.local.dao.CartDao
import com.example.mysouq.data.local.dao.FavoriteDao
import com.example.mysouq.data.local.entity.CartItemEntity
import com.example.mysouq.data.mapper.toDomain
import com.example.mysouq.domain.model.CartItem
import com.example.mysouq.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao,
    private val favoriteDao: FavoriteDao
) : CartRepository {

    override fun observeCart(): Flow<List<CartItem>> = combine(
        cartDao.observeCart(),
        favoriteDao.observeFavoriteIds()
    ) { cartLines, favIds ->
        val favSet = favIds.toSet()
        cartLines.map { it.toDomain(isFavorite = it.id in favSet) }
    }

    override fun observeItemCount(): Flow<Int> = cartDao.observeItemCount()

    override fun observeTotal(): Flow<Double> = cartDao.observeTotal()

    override suspend fun addToCart(productId: Int, quantity: Int) {
        val existing = cartDao.findByProductId(productId)
        if (existing != null) {
            cartDao.updateQuantity(productId, existing.quantity + quantity)
        } else {
            cartDao.upsert(CartItemEntity(productId = productId, quantity = quantity))
        }
    }

    override suspend fun updateQuantity(productId: Int, quantity: Int) {
        if (quantity <= 0) {
            cartDao.remove(productId)
        } else {
            cartDao.updateQuantity(productId, quantity)
        }
    }

    override suspend fun removeFromCart(productId: Int) {
        cartDao.remove(productId)
    }

    override suspend fun clearCart() {
        cartDao.clear()
    }
}
