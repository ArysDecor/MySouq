package com.example.mysouq.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Représentation persistante d'un produit. N'inclut PAS l'état utilisateur
 * (favori / panier) qui vit dans des tables dédiées.
 */
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val region: String,
    val artisan: String,
    val imageUrl: String,
    val rating: Double,
    val reviewCount: Int,
    val isAvailable: Boolean
)
