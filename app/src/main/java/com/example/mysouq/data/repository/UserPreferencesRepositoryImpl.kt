package com.example.mysouq.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mysouq.domain.model.User
import com.example.mysouq.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    private object PreferencesKeys {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val LANGUAGE = stringPreferencesKey("language")
        val ORDER_COUNT = intPreferencesKey("order_count")
        val USER_ID = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_CITY = stringPreferencesKey("user_city")
    }

    override val isDarkMode: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.DARK_MODE] ?: false
        }

    override val language: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.LANGUAGE] ?: "fr"
        }

    override val orderCount: Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferencesKeys.ORDER_COUNT] ?: 0
        }

    override val user: Flow<User?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val id = preferences[PreferencesKeys.USER_ID]
            if (id != null) {
                User(
                    id = id,
                    name = preferences[PreferencesKeys.USER_NAME] ?: "",
                    email = preferences[PreferencesKeys.USER_EMAIL] ?: "",
                    city = preferences[PreferencesKeys.USER_CITY]
                )
            } else {
                null
            }
        }

    override suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_MODE] = enabled
        }
    }

    override suspend fun setLanguage(languageCode: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE] = languageCode
        }
    }

    override suspend fun incrementOrderCount() {
        dataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.ORDER_COUNT] ?: 0
            preferences[PreferencesKeys.ORDER_COUNT] = current + 1
        }
    }

    override suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_ID] = user.id
            preferences[PreferencesKeys.USER_NAME] = user.name
            preferences[PreferencesKeys.USER_EMAIL] = user.email
            user.city?.let { preferences[PreferencesKeys.USER_CITY] = it }
        }
    }

    override suspend fun clearUser() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.USER_ID)
            preferences.remove(PreferencesKeys.USER_NAME)
            preferences.remove(PreferencesKeys.USER_EMAIL)
            preferences.remove(PreferencesKeys.USER_CITY)
        }
    }
}
