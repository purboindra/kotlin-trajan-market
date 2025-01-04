package com.example.trajanmarket.data.local.datastore

import PreferencesKey
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val context: Context) {
    
    suspend fun clear() {
        context.dataStore.edit { preference ->
            preference.clear()
        }
    }
    
    suspend fun remove(key: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.dataStore.edit { preference ->
            preference.remove(preferencesKey)
        }
    }
    
    suspend fun saveUserEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.USER_EMAIL] = email
        }
    }
    
    suspend fun saveUserName(userName: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.USER_NAME] = userName
        }
    }
    
    suspend fun saveUserId(id: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.USER_ID] = id
        }
    }
    
    suspend fun saveAccesToken(accessToken: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.ACCESS_TOKEN] = accessToken
        }
    }
    
    suspend fun saveUserImage(image: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.USER_IMAGE] = image
        }
    }
    
    suspend fun saveIsLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.IS_LOGGED_IN] = isLoggedIn
        }
    }
    
    val userName: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKey.USER_NAME]
    }
    
    val userId: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKey.USER_ID]
    }
    
    val email: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKey.USER_EMAIL]
    }
    
    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKey.IS_LOGGED_IN] ?: false
    }
    
}