package com.example.trajanmarket.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trajanmarket.data.local.datastore.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    userPreferences: UserPreferences,
) : ViewModel() {
    
    private val _navigationEvent = MutableSharedFlow<Boolean>(replay = 1)
    val navigationEvent: SharedFlow<Boolean> = _navigationEvent.asSharedFlow()
    
    init {
        viewModelScope.launch {
            userPreferences.isLoggedIn.collect { isLoggedIn ->
                if (isLoggedIn != null) {
                    delay(2000)
                    _navigationEvent.emit(isLoggedIn)
                }
            }
        }
    }
}