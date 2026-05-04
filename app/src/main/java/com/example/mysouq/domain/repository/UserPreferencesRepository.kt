package com.example.mysouq.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val isDarkMode: Flow<Boolean>
    val language: Flow<String>

    suspend fun setDarkMode(enabled: Boolean)
    suspend fun setLanguage(languageCode: String)
}
