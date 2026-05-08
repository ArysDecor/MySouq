package com.example.mysouq.domain.usecase

import com.example.mysouq.domain.repository.CartRepository
import javax.inject.Inject

class RemoveFromCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(productId: Int) {
        repository.removeFromCart(productId)
    }
}
