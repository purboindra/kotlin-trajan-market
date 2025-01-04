package com.example.trajanmarket.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.trajanmarket.data.local.datastore.UserPreferences
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.domain.appwrite.AppwriteClient
import com.example.trajanmarket.utils.generateSalt
import com.example.trajanmarket.utils.hashPasswordSHA256
import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.exceptions.AppwriteException
import io.appwrite.models.User
import io.appwrite.services.Account
import io.appwrite.services.Databases
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@Serializable
data class ErrorResponse(val message: String)

private val TAG = "AuthRepository"

class AuthRepository(
    private val appwriteDatabase: Databases,
    private val appwriteAccount: Account,
    private val userPreferences: UserPreferences,
) {
    
    private val databaseId = AppwriteClient.DATABASE_ID
    
    private val collectionUsers = AppwriteClient.COLLECTION_USERS
    private val collectionProducts = AppwriteClient.COLLECTION_PRODUCTS
    private val collectionCarts = AppwriteClient.COLLECTION_CARTS
    
    fun getLoggedIn() = flow<State<User<Map<String, Any>>?>> {
        try {
            val user = appwriteAccount.get()
            Log.d(TAG, "Logged in user: $user")
            emit(State.Succes(user))
        } catch (e: AppwriteException) {
            Log.d(TAG, "Error get logged in user: ${e.message}")
            emit(State.Failure(e))
        }
    }
    
    fun login(email: String, password: String): Flow<State<Boolean>> = flow {
        emit(State.Loading)
        Log.d(TAG, "Logging in user: $email $password")
        
        try {
            val session =
                appwriteAccount.createEmailPasswordSession(email = email, password = password)
            Log.d(TAG, "Login session: $session")
            emit(State.Succes(true))
        } catch (e: Exception) {
            Log.d(TAG, "Error during login: ${e.message}")
            emit(State.Failure(e))
        }
    }
    
    
    @RequiresApi(Build.VERSION_CODES.O)
    fun register(
        username: String,
        password: String,
        email: String,
        phoneNumber: String,
        address: String
    ) =
        flow {
            emit(State.Loading)
            
            try {
                val user = try {
                    appwriteAccount.create(ID.unique(), email, password, name = username)
                } catch (e: Exception) {
                    Log.d(TAG, "Error creating user: ${e.message}")
                    emit(State.Failure(e))
                    return@flow
                }
                
                login(email, password)
                
                val date = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                
                val userId = user.id
                
                val salt = generateSalt()
                val hashedPassword = hashPasswordSHA256(password, salt)
                
                val userDocument = mapOf(
                    "id" to userId,
                    "username" to username,
                    "full_name" to username,
                    "password" to hashedPassword,
                    "email" to email,
                    "phone_number" to phoneNumber,
                    "address" to address,
                    "created_at" to date,
                )
                
                appwriteDatabase.createDocument(
                    collectionId = collectionUsers,
                    data = userDocument,
                    databaseId = databaseId,
                    documentId = userId,
                    permissions = listOf()
                )
                
                userPreferences.saveUserId(userId)
                userPreferences.saveUserName(username)
                userPreferences.saveUserEmail(email)
                
                emit(State.Succes(true))
            } catch (e: Exception) {
                Log.d(TAG, "Error registering user: ${e.message}")
                emit(State.Failure(e))
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