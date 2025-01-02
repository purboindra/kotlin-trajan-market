package com.example.trajanmarket.data.repository

import android.util.Log
import com.example.trajanmarket.data.local.datastore.UserPreferences
import com.example.trajanmarket.data.model.LoginResponse
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.data.remote.api.AuthApi
import com.example.trajanmarket.data.remote.service.ParsedClientException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Serializable
data class ErrorResponse(val message: String)

class AuthRepository(
    private val userPreferences: UserPreferences,
    private val authApi: AuthApi
) {
    
    fun login(username: String, password: String): Flow<State<LoginResponse>> = flow {
        emit(State.Loading)
        try {
            val response = authApi.login(username, password)
            if (response.status.isSuccess()) {
                val responseBody = response.bodyAsText()
                val loginResponse: LoginResponse =
                    Json.decodeFromString<LoginResponse>(responseBody)
                userPreferences.saveUserName(loginResponse.username)
                userPreferences.saveUserId(loginResponse.id.toString())


                emit(State.Succes(loginResponse))
            } else {
                val errorText = response.bodyAsText()
                val errorResponse = try {
                    Json.decodeFromString<ErrorResponse>(errorText)
                } catch (e: Exception) {
                    ErrorResponse(e.message ?: "Unknown error occurred")
                }
                emit(State.Failure(Exception(errorResponse.message)))
            }
        } catch (e: ParsedClientException) {
            Log.e("ParsedClientException AuthRepository", e.message)
            emit(State.Failure(Exception(e.message)))
        } catch (e: ClientRequestException) {
            Log.e("ClientRequestException AuthRepository", e.message)
            emit(State.Failure(Exception(e.message)))
        } catch (e: Exception) {
            Log.e("Exception AuthRepository", "Login: ${e.message}")
            emit(State.Failure(Exception("Network error: ${e.localizedMessage}")))
        }
    }
}