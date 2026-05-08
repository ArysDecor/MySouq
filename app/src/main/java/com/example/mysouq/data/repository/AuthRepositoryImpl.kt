package com.example.mysouq.data.repository

import com.example.mysouq.domain.model.User
import com.example.mysouq.domain.model.Result
import com.example.mysouq.domain.repository.AuthRepository
import com.example.mysouq.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import java.util.UUID

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : AuthRepository {
    
    private val _currentUser = MutableStateFlow<User?>(null)
    override val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        // Charger l'utilisateur sauvegardé au démarrage
        CoroutineScope(Dispatchers.IO).launch {
            userPreferencesRepository.user.collectLatest { user ->
                _currentUser.value = user
            }
        }
    }

    override suspend fun login(email: String, password: String): Result<User> {
        delay(1500)
        return if (email.contains("@") && password.length >= 4) {
            val user = User(
                id = UUID.randomUUID().toString(),
                name = email.split("@")[0].replaceFirstChar { it.uppercase() },
                email = email,
                city = "Marrakech"
            )
            userPreferencesRepository.saveUser(user)
            Result.Success(user)
        } else {
            Result.Error(Exception("Email invalide ou mot de passe trop court (min 4)"))
        }
    }

    override suspend fun register(name: String, email: String, password: String): Result<User> {
        delay(1500)
        if (name.isBlank() || !email.contains("@") || password.length < 4) {
            return Result.Error(Exception("Veuillez remplir tous les champs correctement"))
        }

        val user = User(
            id = UUID.randomUUID().toString(),
            name = name,
            email = email,
            city = "Fès"
        )
        userPreferencesRepository.saveUser(user)
        return Result.Success(user)
    }

    override suspend fun logout() {
        userPreferencesRepository.clearUser()
    }
}
