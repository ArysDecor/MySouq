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
            ProductEntity(1, "Tajine Authentique", "Plat traditionnel en terre cuite.", 250.0, Category.POTTERY.name, "Marrakech", "Ahmed", "https://lamaisondupotier.com/cdn/shop/files/plat-a-tajine-beldi-en-terre-cuite-2648518.png?v=1758114837&width=1946", 4.8, 120, true),
            ProductEntity(2, "Tapis Beni Ourain", "Tapis en laine fait main.", 1500.0, Category.TEXTILE.name, "Atlas", "Fatima", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSej-Ez9eRTJZLqDnGbpjax7HHiu2FFYErWwQ&s", 4.9, 45, true),
            ProductEntity(3, "Lanterne Cuivre", "Lanterne", 350.0, Category.DECOR.name, "Fes", "Youssef", "https://st2.depositphotos.com/1177973/6078/i/950/depositphotos_60784707-stock-photo-lantern-in-hands-in-darkness.jpg", 4.7, 89, true),
            ProductEntity(4, "Huile d'Argan", "Huile pure cosmétique.", 150.0, Category.COSMETICS.name, "Agadir", "Coopérative Amal", "https://images.unsplash.com/photo-1608571423902-eed4a5ad8108?q=80&w=800", 4.9, 210, true),
            ProductEntity(5, "Pouf en Cuir", "Pouf traditionnel.", 400.0, Category.LEATHER.name, "Marrakech", "Brahim", "https://e-xportmorocco.com/storage/produits/1732873872.jpeg", 4.6, 56, true),
            ProductEntity(6, "Plat en Céramique", "Plat décoratif émaillé.", 180.0, Category.POTTERY.name, "Safi", "Said", "https://tibladin.dk/wp-content/uploads/2025/10/Lyseblaa-35-cm-fade-400x400.jpeg", 4.5, 34, true),
            ProductEntity(7, "Sac à Main Cuir", "Sac en cuir", 550.0, Category.LEATHER.name, "Fès", "Hassan", "https://i.etsystatic.com/57662862/r/il/0e9eaf/7436732612/il_fullxfull.7436732612_dcqa.jpg", 4.8, 72, true),
            ProductEntity(8, "Miroir Artisanal", "Cadre en bois sculpté.", 600.0, Category.DECOR.name, "Essaouira", "Mustapha", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTwvpwebsusxbJcshKrg6zg7K60yys7N_JBzA&s", 4.7, 28, true),
            ProductEntity(9, "Djellaba Homme", "Coton léger haute qualité.", 450.0, Category.TEXTILE.name, "Chefchaouen", "Driss", "https://djellaba-tradition.com/cdn/shop/files/DjellabaHommeBlancheMancheCourte_1200x1200.jpg?v=1682860314", 4.6, 15, true),
            ProductEntity(10, "Savon Noir", "Soin traditionnel hammam.", 45.0, Category.COSMETICS.name, "Casablanca", "BioSouss", "https://cache.magazine-avantages.fr/data/photo/w1000_c18/4m/savonnoir.jpg", 4.9, 320, true),
            ProductEntity(11, "Vase de Safi", "Céramique bleue mythique.", 300.0, Category.POTTERY.name, "Safi", "Karim", "https://img.leboncoin.fr/api/v1/lbcpb1/images/05/55/f1/0555f1fe06e6de4000703462173b37383ccfef6e.jpg?rule=ad-large", 4.7, 41, true),
            ProductEntity(12, "Babouches", "Cuir souple brodé.", 120.0, Category.LEATHER.name, "Marrakech", "Aziz", "https://benson-shoes.com/_next/image?url=https%3A%2F%2Fcdn.shopify.com%2Fs%2Ffiles%2F1%2F0550%2F5060%2F0700%2Fproducts%2FBCassablancaBelgha_0484.jpg%3Fv%3D1637592691&w=3840&q=75", 4.5, 95, true),
            ProductEntity(13, "Théière Argentée", "Pour le thé à la menthe.", 450.0, Category.DECOR.name, "Fès", "Omar", "https://mytindy.com/cdn/shop/products/zRIrZ9q55G.jpg?v=1634034339", 4.8, 63, true),
            ProductEntity(14, "Couverture Pompons", "Laine de coton tissée.", 850.0, Category.TEXTILE.name, "Marrakech", "Leila", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSBk6GB_9TXfJrIMeKrth6hTffzVb788PRiig&s", 4.9, 19, true),
            ProductEntity(15, "Eau de Rose", "Distillation artisanale.", 80.0, Category.COSMETICS.name, "Kelaat M'Gouna", "Coop Rose", "https://www.epices.com/modules/amazzingblog/views/img/uploads/posts/148/mm/1-644ac52072035.jpg", 4.8, 142, true)
        )
        productDao.insertAll(seedProducts)
    }
}
