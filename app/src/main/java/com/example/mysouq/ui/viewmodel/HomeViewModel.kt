package com.example.mysouq.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysouq.domain.model.Category
import com.example.mysouq.domain.model.Product
import com.example.mysouq.domain.usecase.AddToCartUseCase
import com.example.mysouq.domain.usecase.GetProductsUseCase
import com.example.mysouq.domain.usecase.ToggleFavoriteUseCase
import com.example.mysouq.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.example.mysouq.domain.model.Result

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    val uiState: StateFlow<UiState<List<Product>>> = combine(
        getProductsUseCase(),
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

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun onCategorySelect(category: Category?) {
        _selectedCategory.value = category
    }

    fun toggleFavorite(productId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(productId)
        }
    }

    fun addToCart(productId: Int) {
        viewModelScope.launch {
            addToCartUseCase(productId)
        }
    }
}
