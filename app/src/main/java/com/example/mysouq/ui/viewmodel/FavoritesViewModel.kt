package com.example.mysouq.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysouq.domain.model.Product
import com.example.mysouq.domain.model.Result
import com.example.mysouq.domain.repository.CartRepository
import com.example.mysouq.domain.repository.FavoriteRepository
import com.example.mysouq.domain.repository.ProductRepository
import com.example.mysouq.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val favoriteRepository: FavoriteRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    val uiState: StateFlow<UiState<List<Product>>> = productRepository.observeFavorites()
        .map { result ->
            when (result) {
                is Result.Success -> UiState.Success(result.data)
                is Result.Error -> UiState.Error(result.exception.message)
                is Result.Loading -> UiState.Loading
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UiState.Loading
        )

    fun toggleFavorite(productId: Int) {
        viewModelScope.launch {
            favoriteRepository.toggleFavorite(productId)
        }
    }

    fun addToCart(productId: Int) {
        viewModelScope.launch {
            cartRepository.addToCart(productId)
        }
    }
}
