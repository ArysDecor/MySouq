package com.example.mysouq.domain.repository

import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun observeFavoriteIds(): Flow<Set<Int>>
    suspend fun isFavorite(productId: Int): Boolean
    suspend fun toggleFavorite(productId: Int)
}
