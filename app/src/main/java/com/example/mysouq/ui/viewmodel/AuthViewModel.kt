package com.example.mysouq.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysouq.domain.model.Result
import com.example.mysouq.domain.model.User
import com.example.mysouq.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    val currentUser: StateFlow<User?> = authRepository.currentUser

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            when (val result = authRepository.login(email, password)) {
                is Result.Success -> {
                    _uiState.value = AuthUiState(success = true)
                }
                is Result.Error -> {
                    _uiState.value = AuthUiState(error = result.exception.message)
                }
                else -> {}
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            when (val result = authRepository.register(name, email, password)) {
                is Result.Success -> {
                    _uiState.value = AuthUiState(success = true)
                }
                is Result.Error -> {
                    _uiState.value = AuthUiState(error = result.exception.message)
                }
                else -> {}
            }
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState()
    }
}
