package com.example.mysouq.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val isDarkMode: Flow<Boolean>
    val language: Flow<String>
    val orderCount: Flow<Int>

    suspend fun setDarkMode(enabled: Boolean)
    suspend fun setLanguage(languageCode: String)
    suspend fun incrementOrderCount()
}
