package com.example.mysouq.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysouq.domain.model.CartItem
import com.example.mysouq.domain.repository.CartRepository
import com.example.mysouq.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    val uiState: StateFlow<UiState<List<CartItem>>> = cartRepository.observeCart()
        .map { UiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    val totalPrice: StateFlow<Double> = cartRepository.observeTotal()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun updateQuantity(productId: Int, quantity: Int) {
        viewModelScope.launch {
            cartRepository.updateQuantity(productId, quantity)
        }
    }

    fun removeFromCart(productId: Int) {
        viewModelScope.launch {
            cartRepository.removeFromCart(productId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }
}
