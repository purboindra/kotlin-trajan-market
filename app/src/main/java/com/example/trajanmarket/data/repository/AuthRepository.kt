package com.example.trajanmarket.data.repository

import android.util.Log
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.data.remote.api.AuthApi
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(val token: String)

@Serializable
data class ErrorResponse(val message: String)

class AuthRepository(private val authApi: AuthApi) {
    fun login(username: String, password: String): Flow<State<HttpResponse>> = flow {
        emit(State.Loading)
        try {
            val response = authApi.login(username, password)
            
            Log.d("Response authRepo","Login: ${response.bodyAsText()}")
            
            if (response.status.isSuccess()) {
                emit(State.Succes(response))
            } else {
                val errorText = response.bodyAsText()
                emit(State.Failure(Exception("Error: $errorText")))
            }
        } catch (e: Exception) {
            emit(State.Failure(e))
        }
    }
}