package com.example.mysouq.ui.common

sealed interface UiState<out T> {
    object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String? = null, val exception: Throwable? = null) : UiState<Nothing>
}
