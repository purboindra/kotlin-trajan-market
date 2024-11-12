package com.example.trajanmarket.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AuthApi(private val client: HttpClient) {
    suspend fun login(username: String, password: String): HttpResponse =
        client.post("auth/login") {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "username" to username,
                    "password" to password
                )
            )
        }
}