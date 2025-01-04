package com.example.trajanmarket.ui.screens.register

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trajanmarket.data.local.datastore.UserPreferences
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.domain.appwrite.AppwriteClient
import com.example.trajanmarket.domain.usecases.AuthUseCase
import com.example.trajanmarket.utils.LocationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import io.appwrite.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "RegisterViewModel"

@HiltViewModel
class RegisterViewModel @Inject constructor(
    userPreferences: UserPreferences,
    private val authUseCase: AuthUseCase,
    private val locationHelper: LocationHelper,
    
    ) : ViewModel() {
    
    private val _registerState = MutableStateFlow<State<Boolean>>(State.Idle)
    val registerState = _registerState.asStateFlow()
    
    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName
    
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password
    
    private val _showPassword = MutableStateFlow(false)
    val showPassword: StateFlow<Boolean> = _showPassword
    
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email
    
    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber
    
    private val _userNameError = MutableStateFlow<String?>(null)
    val userNameError: StateFlow<String?> = _userNameError
    
    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError
    
    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError
    
    private val _phoneNumberError = MutableStateFlow<String?>(null)
    val phoneNumberError: StateFlow<String?> = _phoneNumberError
    
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
    
    fun onEmailChange(value: String) {
        _email.update { value }
        _emailError.update { if (value.isBlank()) "Email cannot be empty" else null }
    }
    
    fun onPhoneNumberError(value: String) {
        _phoneNumber.update { value }
        _phoneNumberError.update { if (value.isBlank()) "Phone number cannot be empty" else null }
    }
    
    fun register() {
        viewModelScope.launch {
            authUseCase.register(
                username = _userName.value,
                password = _password.value,
                email = _email.value,
                phoneNumber = _phoneNumber.value,
                _address.value,
            ).collectLatest { state ->
                _registerState.value = state
            }
        }
    }
    
}