package com.example.mysouq.data.repository

import com.example.mysouq.data.local.dao.FavoriteDao
import com.example.mysouq.data.local.entity.FavoriteEntity
import com.example.mysouq.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {

    override fun observeFavoriteIds(): Flow<Set<Int>> =
        favoriteDao.observeFavoriteIds().map { it.toSet() }

    override suspend fun isFavorite(productId: Int): Boolean =
        favoriteDao.isFavorite(productId)

    override suspend fun toggleFavorite(productId: Int) {
        if (isFavorite(productId)) {
            favoriteDao.remove(productId)
        } else {
            favoriteDao.add(FavoriteEntity(productId = productId))
        }
    }
}
