package com.example.trajanmarket.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AuthApi(private val client: HttpClient) {
    suspend fun login(username: String, password: String): HttpResponse =
        client.post("https://your-api-url.com/login") {
            setBody(
                Json.encodeToString(
                    mapOf(
                        "username" to username,
                        "password" to password
                    )
                )
            )
        }
    
}