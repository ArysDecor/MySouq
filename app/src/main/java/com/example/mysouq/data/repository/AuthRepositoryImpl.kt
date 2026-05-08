package com.example.mysouq.data.repository

import com.example.mysouq.domain.model.User
import com.example.mysouq.domain.model.Result
import com.example.mysouq.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    
    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    override suspend fun login(email: String, password: String): Result<User> {
        delay(1500) // Simulation d'appel API
        return if (email == "test@mysouq.com" && password == "password") {
            val user = User(
                id = "1",
                name = "Yahya Artisan",
                email = email,
                city = "Marrakech"
            )
            _currentUser.value = user
            Result.Success(user)
        } else {
            Result.Error(Exception("Email ou mot de passe incorrect"))
        }
    }

    override suspend fun register(name: String, email: String, password: String): Result<User> {
        delay(1500)
        val user = User(
            id = "2",
            name = name,
            email = email,
            city = "Fès"
        )
        _currentUser.value = user
        return Result.Success(user)
    }

    override suspend fun logout() {
        _currentUser.value = null
    }
}
