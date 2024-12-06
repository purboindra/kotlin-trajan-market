package com.example.trajanmarket.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.trajanmarket.data.local.datastore.UserPreferences
import com.example.trajanmarket.ui.navigation.Login
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val userPreferences: UserPreferences,
) : ViewModel() {
    
    private val _loadingLogout = MutableStateFlow(false)
    val loadingLogout: StateFlow<Boolean> = _loadingLogout
    
    fun logout(navHostController: NavHostController) = viewModelScope.launch {
        _loadingLogout.emit(true)
        delay(1000)
        userPreferences.clear()
        _loadingLogout.emit(false)
        navHostController.navigate(
            Login
        )
    }
}