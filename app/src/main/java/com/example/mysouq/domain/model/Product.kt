package com.example.mysouq.domain.model

/**
 * Modèle de domaine Produit. Indépendant de la couche persistance.
 * Les états utilisateur (favori, panier) sont portés par [CartItem] / favori séparés.
 */
data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val category: Category,
    val region: String,
    val artisan: String,
    val imageUrl: String,
    val rating: Double,
    val reviewCount: Int,
    val isAvailable: Boolean,
    val isFavorite: Boolean = false,
    val cartQuantity: Int = 0
) {
    val inCart: Boolean get() = cartQuantity > 0
}
