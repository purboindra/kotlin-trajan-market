package com.example.trajanmarket.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class CartApi(private val client: HttpClient) {
    suspend fun addToCart(products: List<Map<String, Any>>, userId: String): HttpResponse =
        client.post("carts/add") {
            contentType(ContentType.Application.Json)
            setBody(
                mapOf(
                    "userId" to userId,
                    "products" to products,
                )
            )
        }
    
}

