package com.example.mysouq.data.mapper

import com.example.mysouq.data.local.dao.CartLine
import com.example.mysouq.data.local.entity.ProductEntity
import com.example.mysouq.domain.model.CartItem
import com.example.mysouq.domain.model.Category
import com.example.mysouq.domain.model.Product

fun ProductEntity.toDomain(
    isFavorite: Boolean = false,
    cartQuantity: Int = 0
): Product = Product(
    id = id,
    name = name,
    description = description,
    price = price,
    category = Category.fromName(category) ?: Category.DECOR,
    region = region,
    artisan = artisan,
    imageUrl = imageUrl,
    rating = rating,
    reviewCount = reviewCount,
    isAvailable = isAvailable,
    isFavorite = isFavorite,
    cartQuantity = cartQuantity
)

fun Product.toEntity(): ProductEntity = ProductEntity(
    id = id,
    name = name,
    description = description,
    price = price,
    category = category.name,
    region = region,
    artisan = artisan,
    imageUrl = imageUrl,
    rating = rating,
    reviewCount = reviewCount,
    isAvailable = isAvailable
)

fun CartLine.toDomain(
    isFavorite: Boolean = false
): CartItem = CartItem(
    product = Product(
        id = id,
        name = name,
        description = description,
        price = price,
        category = Category.fromName(category) ?: Category.DECOR,
        region = region,
        artisan = artisan,
        imageUrl = imageUrl,
        rating = rating,
        reviewCount = reviewCount,
        isAvailable = isAvailable,
        isFavorite = isFavorite,
        cartQuantity = quantity
    ),
    quantity = quantity
)
