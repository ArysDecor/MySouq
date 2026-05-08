package com.example.mysouq.domain.usecase

import com.example.mysouq.domain.repository.CartRepository
import com.example.mysouq.domain.repository.UserPreferencesRepository
import javax.inject.Inject

class CheckoutUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke() {
        // Here we could add logic to create a real order entity
        cartRepository.clearCart()
        userPreferencesRepository.incrementOrderCount()
    }
}
