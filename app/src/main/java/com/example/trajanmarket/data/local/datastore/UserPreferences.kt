package com.example.trajanmarket.data.local.datastore

import PreferencesKey
import android.content.Context
import androidx.datastore.preferences.core.edit
import dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences(private val context: Context) {
    suspend fun saveUserName(userName: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.USER_NAME] = userName
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
    
    val isLoggedIn: Flow<Boolean?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKey.IS_LOGGED_IN]
    }
    
}