package com.example.mysouq.domain.usecase

import com.example.mysouq.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartItemCountUseCase @Inject constructor(
    private val repository: CartRepository
) {
    operator fun invoke(): Flow<Int> {
        return repository.observeItemCount()
    }
}
