package com.example.trajanmarket.data.repository

import android.util.Log
import com.example.trajanmarket.data.local.datastore.UserPreferences
import com.example.trajanmarket.data.model.LoginResponse
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.data.remote.api.AuthApi
import com.example.trajanmarket.data.remote.service.ParsedClientException
import io.appwrite.ID
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.User
import io.appwrite.services.Account
import io.appwrite.services.Databases
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Serializable
data class ErrorResponse(val message: String)

private val TAG = "AuthRepository"

class AuthRepository(
    private val userPreferences: UserPreferences,
    private val authApi: AuthApi,
    private val appwriteDatabase: Databases,
    private val appwriteAccount: Account
) {
    
    private fun getLoggedIn() = flow<State<User<Map<String, Any>>?>> {
        try {
            val user = appwriteAccount.get()
            emit(State.Succes(user))
        } catch (e: AppwriteException) {
            Log.d(TAG, "Error getting logged in user: ${e.message}")
            emit(State.Failure(e))
        }
    }
    
    
    @OptIn(ExperimentalCoroutinesApi::class)
    fun login(email: String, password: String): Flow<State<User<Map<String, Any>>?>> = flow {
        emit(State.Loading)
        appwriteAccount.createEmailPasswordSession(email, password)
    }.flatMapConcat {
        getLoggedIn()
    }.catch { e ->
        Log.d(TAG, "Error getting logged in user: ${e.message}")
        emit(State.Failure(e))
    }
    
    
    fun register(username: String, password: String, email: String, phoneNumber: String) =
        flow {
            emit(State.Loading)
            try {
                val userFlow = try {
                    appwriteAccount.create(ID.unique(), email, password)
                    login(email, password)
                } catch (e: Exception) {
                    Log.d(TAG, "Error creating user: ${e.message}")
                    emit(State.Failure(e))
                    return@flow
                }
                
                userFlow.collect { state ->
                    when (state) {
                        is State.Succes -> {
                            val user = state.data
                            val userId = user?.id
                            val userDocument = mapOf(
                                "userId" to userId,
                                "username" to username,
                                "email" to email,
                                "phoneNumber" to phoneNumber
                            )
                            try {
                                appwriteDatabase.createDocument(
                                    collectionId = "616f3b3b7f5b4",
                                    data = userDocument,
                                    databaseId = "your_database_id",
                                    documentId = ID.unique(),
                                    permissions = listOf()          // Replace TODO() with appropriate permissions
                                )
                                emit(State.Succes(true)) // Successfully created the user and the document
                            } catch (e: Exception) {
                                Log.d(TAG, "Error creating user document: ${e.message}")
                                emit(State.Failure(e)) // Handle document creation failure
                            }
                        }
                        
                        is State.Failure -> {
                            emit(State.Failure(state.throwable)) // Propagate the error state
                        }
                        
                        else -> {
                            emit(State.Loading) // Emit loading state if necessary
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "Error registering user: ${e.message}")
                emit(State.Failure(e)) // Catch any remaining errors
            }
            
        }

//    fun login(username: String, password: String): Flow<State<LoginResponse>> = flow {
//        emit(State.Loading)
//        try {
//            val response = authApi.login(username, password)
//            if (response.status.isSuccess()) {
//                val responseBody = response.bodyAsText()
//                val loginResponse: LoginResponse =
//                    Json.decodeFromString<LoginResponse>(responseBody)
//                userPreferences.saveUserName(loginResponse.username)
//                userPreferences.saveUserId(loginResponse.id.toString())
//
//
//                emit(State.Succes(loginResponse))
//            } else {
//                val errorText = response.bodyAsText()
//                val errorResponse = try {
//                    Json.decodeFromString<ErrorResponse>(errorText)
//                } catch (e: Exception) {
//                    ErrorResponse(e.message ?: "Unknown error occurred")
//                }
//                emit(State.Failure(Exception(errorResponse.message)))
//            }
//        } catch (e: ParsedClientException) {
//            Log.e("ParsedClientException AuthRepository", e.message)
//            emit(State.Failure(Exception(e.message)))
//        } catch (e: ClientRequestException) {
//            Log.e("ClientRequestException AuthRepository", e.message)
//            emit(State.Failure(Exception(e.message)))
//        } catch (e: Exception) {
//            Log.e("Exception AuthRepository", "Login: ${e.message}")
//            emit(State.Failure(Exception("Network error: ${e.localizedMessage}")))
//        }
//    }
}