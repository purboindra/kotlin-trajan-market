package com.example.trajanmarket.data.repository

import android.util.Log
import com.example.trajanmarket.data.model.LoginResponse
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.data.remote.api.AuthApi
import com.example.trajanmarket.data.remote.service.ParsedClientException
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json



@Serializable
data class ErrorResponse(val message: String)

class AuthRepository(private val authApi: AuthApi) {

    fun login(username: String, password: String): Flow<State<LoginResponse>> = flow {
        emit(State.Loading)
        try {
            val response = authApi.login(username, password)
            Log.d("Response AuthRepository","Login: ${response.status}")
            if (response.status.isSuccess()) {
                val responseBody = response.bodyAsText()
                val loginResponse: LoginResponse = Json.decodeFromString<LoginResponse>(responseBody)
                Log.d("isSuccess AuthRepository", response.bodyAsText())
                Log.d("isSuccess AuthRepository", response.body())
                emit(State.Succes(loginResponse))
            } else {
                val errorText = response.bodyAsText()
                val errorResponse = try {
                    Json.decodeFromString<ErrorResponse>(errorText)
                } catch (e: Exception) {
                    ErrorResponse("An unknown error occurred")
                }

                Log.e("!isSuccess AuthRepository", "Login: ${errorResponse.message}")

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