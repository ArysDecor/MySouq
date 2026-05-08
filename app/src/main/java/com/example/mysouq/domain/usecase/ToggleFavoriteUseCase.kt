package com.example.mysouq.domain.usecase

import com.example.mysouq.domain.repository.FavoriteRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {
    suspend operator fun invoke(productId: Int) {
        repository.toggleFavorite(productId)
    }
}
