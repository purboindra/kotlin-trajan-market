package com.example.trajanmarket.domain.usecases

import com.example.trajanmarket.data.model.LoginResponse
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.data.repository.AuthRepository
import io.appwrite.models.User
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    fun login(username: String, password: String): Flow<State<User<Map<String, Any>>?>> =
        authRepository.login(username, password)
    
    fun getLoggedIn(): Flow<State<User<Map<String, Any>>?>> = authRepository.getLoggedIn()
    
    fun register(
        username: String,
        password: String,
        email: String,
        phoneNumber: String
    ): Flow<State<Boolean>> = authRepository.register(username, password, email, phoneNumber)
}