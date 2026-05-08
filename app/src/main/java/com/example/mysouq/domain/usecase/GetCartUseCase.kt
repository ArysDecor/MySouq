package com.example.mysouq.domain.usecase

import com.example.mysouq.domain.model.CartItem
import com.example.mysouq.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    private val repository: CartRepository
) {
    operator fun invoke(): Flow<List<CartItem>> {
        return repository.observeCart()
    }
}
