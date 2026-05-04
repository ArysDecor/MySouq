package com.example.mysouq.data.repository

import com.example.mysouq.data.local.dao.CartDao
import com.example.mysouq.data.local.dao.FavoriteDao
import com.example.mysouq.data.local.dao.ProductDao
import com.example.mysouq.data.local.entity.ProductEntity
import com.example.mysouq.data.mapper.toDomain
import com.example.mysouq.domain.model.Category
import com.example.mysouq.domain.model.Product
import com.example.mysouq.domain.model.Result
import com.example.mysouq.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val favoriteDao: FavoriteDao,
    private val cartDao: CartDao
) : ProductRepository {

    override fun observeAll(): Flow<Result<List<Product>>> = combine(
        productDao.observeAll(),
        favoriteDao.observeFavoriteIds(),
        cartDao.observeCart()
    ) { products, favIds, cartLines ->
        val favSet = favIds.toSet()
        val cartMap = cartLines.associate { it.id to it.quantity }
        val domainProducts = products.map { entity ->
            entity.toDomain(
                isFavorite = entity.id in favSet,
                cartQuantity = cartMap[entity.id] ?: 0
            )
        }
        Result.Success(domainProducts) as Result<List<Product>>
    }.catch { emit(Result.Error(it)) }

    override fun observeById(id: Int): Flow<Result<Product>> = combine(
        productDao.observeById(id),
        favoriteDao.observeFavoriteIds(),
        cartDao.observeCart()
    ) { entity, favIds, cartLines ->
        if (entity != null) {
            val product = entity.toDomain(
                isFavorite = entity.id in favIds.toSet(),
                cartQuantity = cartLines.firstOrNull { it.id == entity.id }?.quantity ?: 0
            )
            Result.Success(product) as Result<Product>
        } else {
            Result.Error(Exception("Product not found"))
        }
    }.catch { emit(Result.Error(it)) }

    override fun observeFavorites(): Flow<Result<List<Product>>> =
        observeAll().map { result ->
            if (result is Result.Success) {
                Result.Success(result.data.filter { it.isFavorite })
            } else result
        }

    override suspend fun getById(id: Int): Result<Product> {
        return try {
            val entity = productDao.getById(id)
            if (entity != null) {
                Result.Success(entity.toDomain())
            } else {
                Result.Error(Exception("Product not found"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun seedDatabase() {
        if (productDao.count() > 0) return

        val seedProducts = listOf(
            ProductEntity(1, "Tajine Authentique", "Plat traditionnel en terre cuite.", 250.0, Category.POTTERY.name, "Marrakech", "Ahmed", "https://images.unsplash.com/photo-1541535881109-724497679ca4?q=80&w=800", 4.8, 120, true),
            ProductEntity(2, "Tapis Beni Ourain", "Tapis en laine fait main.", 1500.0, Category.TEXTILE.name, "Atlas", "Fatima", "https://images.unsplash.com/photo-1576016770956-debb63d9df05?q=80&w=800", 4.9, 45, true),
            ProductEntity(3, "Lanterne Cuivre", "Lanterne ciselée à la main.", 350.0, Category.DECOR.name, "Fès", "Youssef", "https://images.unsplash.com/photo-1517479149777-5f3b1511d5ad?q=80&w=800", 4.7, 89, true),
            ProductEntity(4, "Huile d'Argan", "Huile pure cosmétique.", 150.0, Category.COSMETICS.name, "Agadir", "Coopérative Amal", "https://images.unsplash.com/photo-1608571423902-eed4a5ad8108?q=80&w=800", 4.9, 210, true),
            ProductEntity(5, "Pouf en Cuir", "Pouf traditionnel brodé.", 400.0, Category.LEATHER.name, "Marrakech", "Brahim", "https://images.unsplash.com/photo-1621360841013-c7683c659ec6?q=80&w=800", 4.6, 56, true),
            ProductEntity(6, "Plat en Céramique", "Plat décoratif émaillé.", 180.0, Category.POTTERY.name, "Safi", "Said", "https://images.unsplash.com/photo-1526434426615-1abe81efcb0b?q=80&w=800", 4.5, 34, true),
            ProductEntity(7, "Sac à Main Cuir", "Sac en cuir véritable.", 550.0, Category.LEATHER.name, "Fès", "Hassan", "https://images.unsplash.com/photo-1548036328-c9fa89d128fa?q=80&w=800", 4.8, 72, true),
            ProductEntity(8, "Miroir Artisanal", "Cadre en bois sculpté.", 600.0, Category.DECOR.name, "Essaouira", "Mustapha", "https://images.unsplash.com/photo-1618220179428-22790b461013?q=80&w=800", 4.7, 28, true),
            ProductEntity(9, "Djellaba Homme", "Coton léger haute qualité.", 450.0, Category.TEXTILE.name, "Chefchaouen", "Driss", "https://images.unsplash.com/photo-1589156229687-496a31ad1d1f?q=80&w=800", 4.6, 15, true),
            ProductEntity(10, "Savon Noir", "Soin traditionnel hammam.", 45.0, Category.COSMETICS.name, "Casablanca", "BioSouss", "https://images.unsplash.com/photo-1600857062241-99e5daec97d1?q=80&w=800", 4.9, 320, true),
            ProductEntity(11, "Vase de Safi", "Céramique bleue mythique.", 300.0, Category.POTTERY.name, "Safi", "Karim", "https://images.unsplash.com/photo-1578500494198-246f612d3b3d?q=80&w=800", 4.7, 41, true),
            ProductEntity(12, "Babouches", "Cuir souple brodé.", 120.0, Category.LEATHER.name, "Marrakech", "Aziz", "https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?q=80&w=800", 4.5, 95, true),
            ProductEntity(13, "Théière Argentée", "Pour le thé à la menthe.", 450.0, Category.DECOR.name, "Fès", "Omar", "https://images.unsplash.com/photo-1576092762791-dd9e2220abd1?q=80&w=800", 4.8, 63, true),
            ProductEntity(14, "Couverture Pompons", "Laine de coton tissée.", 850.0, Category.TEXTILE.name, "Marrakech", "Leila", "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?q=80&w=800", 4.9, 19, true),
            ProductEntity(15, "Eau de Rose", "Distillation artisanale.", 80.0, Category.COSMETICS.name, "Kelaat M'Gouna", "Coop Rose", "https://images.unsplash.com/photo-1556229167-73133606a246?q=80&w=800", 4.8, 142, true)
        )
        productDao.insertAll(seedProducts)
    }
}
