package com.example.mysouq.domain.model

/**
 * Ligne de panier : un produit + une quantité.
 */
data class CartItem(
    val product: Product,
    val quantity: Int
) {
    val subtotal: Double get() = product.price * quantity
}
