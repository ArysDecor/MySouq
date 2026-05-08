package com.example.mysouq.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mysouq.domain.model.User
import com.example.mysouq.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = true,
    val orderCount: Int = 0
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        combine(
            userPreferencesRepository.user,
            userPreferencesRepository.orderCount
        ) { user, count ->
            _uiState.update { it.copy(user = user, orderCount = count, isLoading = false) }
        }.launchIn(viewModelScope)
    }

    fun logout() {
        viewModelScope.launch {
            userPreferencesRepository.clearUser()
        }
    }
}
