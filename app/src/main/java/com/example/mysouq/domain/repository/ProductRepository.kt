package com.example.mysouq.domain.repository

import com.example.mysouq.domain.model.Product
import com.example.mysouq.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun observeAll(): Flow<Result<List<Product>>>
    fun observeById(id: Int): Flow<Result<Product>>
    fun observeFavorites(): Flow<Result<List<Product>>>
    suspend fun getById(id: Int): Result<Product>
    suspend fun seedDatabase()
}
