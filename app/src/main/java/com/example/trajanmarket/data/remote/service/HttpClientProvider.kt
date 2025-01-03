package com.example.trajanmarket.data.remote.service

import android.util.Log
import com.example.trajanmarket.BuildConfig
import com.example.trajanmarket.data.model.CustomResponseException
import com.example.trajanmarket.data.model.Error
import com.example.trajanmarket.data.model.MissingPageException
import com.example.trajanmarket.data.repository.ErrorResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json

class ParsedClientException(
    val response: HttpResponse,
    override val message: String
) : Exception(message)

object HttpClientProvider {
    val client: HttpClient by lazy {
        HttpClient {
            expectSuccess = true
            HttpResponseValidator {
                validateResponse { response ->
                    if (!response.status.isSuccess()) {
                        val errorText = response.bodyAsText()
                        Log.d("validateResponse HttpResponseValidator", "Error Text: $errorText")
                        
                        val errorResponse = try {
                            Json.decodeFromString<ErrorResponse>(errorText)
                        } catch (e: Exception) {
                            ErrorResponse("An unknown error occurred")
                        }
                        
                        Log.e("ValidateResponse", "Error Code: ${response.status}, Message: ${errorResponse.message}")
                        throw CustomResponseException(
                            response,
                            "Code: ${response.status}, message: ${errorResponse.message}"
                        )
                    }
                }
                
                handleResponseExceptionWithRequest { exception, _ ->
                    val clientException = exception as? ClientRequestException
                        ?: return@handleResponseExceptionWithRequest
                    val exceptionResponse = clientException.response
                    val exceptionResponseText = exceptionResponse.bodyAsText()
                    
                    val errorResponse = try {
                        Json.decodeFromString<ErrorResponse>(exceptionResponseText)
                    } catch (e: Exception) {
                        ErrorResponse("An unknown error occurred")
                    }
                    
                    Log.e("Exception HttpClientProvider", errorResponse.message)
                    
                    throw ParsedClientException(
                        exceptionResponse,
                        errorResponse.message
                    )
                }
            }
            
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                level = LogLevel.BODY
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
                connectTimeoutMillis = 15_000
                socketTimeoutMillis = 15_000
            }
            defaultRequest {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                url {
                    protocol = URLProtocol.HTTPS
                    url("")
                }
            }
        }
    }
}