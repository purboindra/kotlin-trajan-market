package com.example.trajanmarket.domain.usecases

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.trajanmarket.data.model.LoginResponse
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.data.repository.AuthRepository
import io.appwrite.models.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    fun login(username: String, password: String): Flow<State<Boolean>> =
        authRepository.login(username, password)
    
    fun getLoggedIn(): Flow<State<User<Map<String, Any>>?>> = authRepository.getLoggedIn()
    
    @RequiresApi(Build.VERSION_CODES.O)
    fun register(
        username: String,
        password: String,
        email: String,
        phoneNumber: String,
        address: String,
    ): Flow<State<Boolean>> =
        authRepository.register(username, password, email, phoneNumber, address)
    
    fun logout(): Flow<State<Boolean>> = authRepository.logout()
}