package com.example.mysouq.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysouq.domain.model.Category
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
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val favoriteRepository: FavoriteRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    val uiState: StateFlow<UiState<List<Product>>> = combine(
        productRepository.observeAll(),
        _searchQuery,
        _selectedCategory
    ) { result, query, category ->
        when (result) {
            is Result.Success -> {
                val filtered = result.data.filter { product ->
                    (category == null || product.category == category) &&
                    (query.isEmpty() || product.name.contains(query, ignoreCase = true) || 
                     product.description.contains(query, ignoreCase = true))
                }
                UiState.Success(filtered)
            }
            is Result.Error -> UiState.Error(result.exception.message)
            is Result.Loading -> UiState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState.Loading
    )

    init {
        viewModelScope.launch {
            productRepository.seedDatabase()
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelect(category: Category?) {
        _selectedCategory.value = category
    }

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
