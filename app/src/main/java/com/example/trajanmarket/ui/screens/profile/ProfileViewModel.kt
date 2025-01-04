package com.example.trajanmarket.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.trajanmarket.data.local.datastore.UserPreferences
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.domain.usecases.AuthUseCase
import com.example.trajanmarket.ui.navigation.Login
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val userPreferences: UserPreferences,
    private val authUseCase: AuthUseCase,
) : ViewModel() {
    
    private val _logoutState = MutableStateFlow<State<Boolean>>(State.Idle)
    val logoutState = _logoutState.asStateFlow()
    
    fun logout() {
        viewModelScope.launch {
            authUseCase.logout().collectLatest { state ->
                _logoutState.value = state
            }
        }
    }
}