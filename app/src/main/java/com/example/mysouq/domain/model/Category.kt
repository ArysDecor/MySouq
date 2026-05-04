package com.example.mysouq.domain.model

/**
 * Catégories de produits artisanaux marocains.
 * Le nom stocké en base est [name] (stable), [displayName] est réservé à l'affichage.
 */
enum class Category(
    val displayName: String,
    val icon: String,
    val tag: String
) {
    POTTERY("Poterie", "🏺", "Artisanat"),
    TEXTILE("Textile", "🧵", "Tissage"),
    JEWELRY("Bijoux", "💎", "Orfèvrerie"),
    LEATHER("Cuir", "👜", "Maroquinerie"),
    WOOD("Bois", "🪵", "Sculpture"),
    COSMETICS("Cosmétique", "🌿", "Naturel"),
    DECOR("Décor", "🏮", "Maison"),
    ART("Art", "🎨", "Peinture");

    companion object {
        fun fromName(name: String?): Category? =
            entries.firstOrNull { it.name == name }
    }
}
