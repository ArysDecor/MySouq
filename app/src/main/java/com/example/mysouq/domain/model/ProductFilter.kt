package com.example.mysouq.domain.model

/**
 * Agrégat des filtres appliqués sur la liste produits.
 */
data class ProductFilter(
    val searchQuery: String = "",
    val category: Category? = null,
    val region: String? = null,
    val maxPrice: Double? = null,
    val minRating: Double = 0.0,
    val onlyAvailable: Boolean = false
) {
    fun isDefault(): Boolean =
        searchQuery.isBlank() &&
                category == null &&
                region == null &&
                maxPrice == null &&
                minRating == 0.0 &&
                !onlyAvailable
}
