package com.example.trajanmarket.ui.screens.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trajanmarket.data.local.datastore.UserPreferences
import com.example.trajanmarket.data.model.LoginResponse
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.domain.usecases.AuthUseCase
import com.example.trajanmarket.utils.LocationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import io.appwrite.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "LoginViewModel"

@HiltViewModel
class LoginViewModel @Inject constructor(
    userPreferences: UserPreferences,
    private val authUseCase: AuthUseCase,
    private val locationHelper: LocationHelper,
) : ViewModel() {

    val userNamePref: Flow<String?> = userPreferences.userName

    private val _loginState = MutableStateFlow<State<User<Map<String, Any>>?>>(State.Idle)
    val loginState: StateFlow<State<User<Map<String, Any>>?>> = _loginState

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _showPassword = MutableStateFlow(false)
    val showPassword: StateFlow<Boolean> = _showPassword

    private val _userNameError = MutableStateFlow<String?>(null)
    val userNameError: StateFlow<String?> = _userNameError

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address

    fun setAddress() {
        viewModelScope.launch {
            val response = locationHelper.getCurrentAddress()
            if (response != null) {
                _address.value = response
            } else {
                Log.d(TAG, "Address null")
            }
        }
    }

    fun toggleShowPassword() {
        _showPassword.update { !it }
    }

    fun onUserNameErrorChange(text: String?) {
        _userNameError.update { text }
    }

    fun onPasswordErrorChange(text: String?) {
        _passwordError.update { text }
    }

    fun onUserNameChange(value: String) {
        _userName.update { value }
        _userNameError.update { if (value.isBlank()) "Username cannot be empty" else null }
    }

    fun onPasswordChange(value: String) {
        _password.update { value }
        _passwordError.update { if (value.isBlank()) "Password cannot be empty" else null }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            authUseCase.login(username, password).collectLatest { state ->
                _loginState.value = state
            }
        }
    }
}