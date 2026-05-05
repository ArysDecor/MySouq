package com.example.mysouq.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "favorites",
    primaryKeys = ["productId"],
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FavoriteEntity(
    val productId: Int,
    val addedAt: Long = System.currentTimeMillis()
)
