package com.example.mysouq.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysouq.domain.model.CartItem
import com.example.mysouq.domain.repository.UserPreferencesRepository
import com.example.mysouq.domain.usecase.*
import com.example.mysouq.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val getCartTotalUseCase: GetCartTotalUseCase,
    private val getCartItemCountUseCase: GetCartItemCountUseCase,
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val checkoutUseCase: CheckoutUseCase,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<UiState<List<CartItem>>> = getCartUseCase()
        .map { UiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    val totalPrice: StateFlow<Double> = getCartTotalUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val itemCount: StateFlow<Int> = getCartItemCountUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val orderCount: StateFlow<Int> = userPreferencesRepository.orderCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun updateQuantity(productId: Int, quantity: Int) {
        viewModelScope.launch {
            updateCartQuantityUseCase(productId, quantity)
        }
    }

    fun removeFromCart(productId: Int) {
        viewModelScope.launch {
            removeFromCartUseCase(productId)
        }
    }

    fun checkout() {
        viewModelScope.launch {
            checkoutUseCase()
        }
    }
}
