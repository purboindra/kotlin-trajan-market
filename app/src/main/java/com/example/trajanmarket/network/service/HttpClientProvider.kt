package com.example.trajanmarket.network.service

import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation


object HttpClientProvider {
    val client:HttpClient by lazy {
        HttpClient{
            install(ContentNegotiation)
        }
    }
}