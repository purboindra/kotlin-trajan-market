package com.example.trajanmarket.domain.usecases

import com.example.trajanmarket.data.model.LoginResponse
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend fun login(username: String, password: String): Flow<State<LoginResponse>> =
        authRepository.login(username, password)
}