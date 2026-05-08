package com.example.mysouq.domain.repository

import com.example.mysouq.domain.model.User
import com.example.mysouq.domain.model.Result
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val currentUser: StateFlow<User?>
    
    suspend fun login(email: String, password: String): Result<User>
    suspend fun register(name: String, email: String, password: String): Result<User>
    suspend fun logout()
}
