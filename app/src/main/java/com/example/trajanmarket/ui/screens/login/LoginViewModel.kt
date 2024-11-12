package com.example.trajanmarket.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.data.repository.AuthRepository
import com.example.trajanmarket.data.repository.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<State<LoginResponse>>(State.Idle)
    val loginState: StateFlow<State<LoginResponse>> = _loginState

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _showPassword = MutableStateFlow(false)
    val showPassword: StateFlow<Boolean> = _showPassword

    fun toggleShowPassword() {
        _showPassword.update { !it }
    }

    fun onUserNameChange(value: String) {
        _userName.update { value }
    }

    fun onPasswordChange(value: String) {
        _password.update { value }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            authRepository.login(username, password).collectLatest { state ->
                _loginState.value = state
            }
        }

    }
}