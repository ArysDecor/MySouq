package com.example.mysouq.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysouq.domain.model.CartItem
import com.example.mysouq.domain.model.User
import com.example.mysouq.domain.repository.UserPreferencesRepository
import com.example.mysouq.domain.usecase.GetCartTotalUseCase
import com.example.mysouq.domain.usecase.GetCartUseCase
import com.example.mysouq.domain.usecase.CheckoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CheckoutUiState(
    val cartItems: List<CartItem> = emptyList(),
    val total: Double = 0.0,
    val user: User? = null,
    val selectedAddress: String = "",
    val selectedPaymentMethod: String = "Carte Bancaire",
    val isLoading: Boolean = false,
    val isOrderPlaced: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val getCartTotalUseCase: GetCartTotalUseCase,
    private val checkoutUseCase: CheckoutUseCase,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        combine(
            getCartUseCase(),
            getCartTotalUseCase(),
            userPreferencesRepository.user
        ) { items, total, user ->
            _uiState.update { it.copy(
                cartItems = items,
                total = total,
                user = user,
                selectedAddress = user?.city ?: ""
            ) }
        }.launchIn(viewModelScope)
    }

    fun onAddressChange(address: String) {
        _uiState.update { it.copy(selectedAddress = address) }
    }

    fun onPaymentMethodChange(method: String) {
        _uiState.update { it.copy(selectedPaymentMethod = method) }
    }

    fun placeOrder() {
        viewModelScope.launch {
            if (_uiState.value.selectedAddress.isBlank()) {
                _uiState.update { it.copy(error = "Veuillez entrer une adresse de livraison") }
                return@launch
            }
            
            _uiState.update { it.copy(isLoading = true, error = null) }
            // Simulation d'une commande
            checkoutUseCase()
            _uiState.update { it.copy(isLoading = false, isOrderPlaced = true) }
        }
    }
}
