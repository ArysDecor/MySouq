package com.example.mysouq.domain.repository

import com.example.mysouq.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val isDarkMode: Flow<Boolean>
    val language: Flow<String>
    val orderCount: Flow<Int>
    val user: Flow<User?>

    suspend fun setDarkMode(enabled: Boolean)
    suspend fun setLanguage(languageCode: String)
    suspend fun incrementOrderCount()
    suspend fun saveUser(user: User)
    suspend fun clearUser()
}
