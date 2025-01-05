package com.example.trajanmarket.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
data class AddToCartParams(
    val id: String,
    val quantity: Int,
    val price: Double
)

@Serializable
data class AddToCartRequestParams(
    val userId: String,
    val products: List<AddToCartParams>
)

class CartApi(private val client: HttpClient) {
    suspend fun addToCart(products: List<AddToCartParams>, userId: String): HttpResponse =
        client.post("carts/add") {
            contentType(ContentType.Application.Json)
            setBody(
                AddToCartRequestParams(
                    userId,
                    products,
                )
            )
        }
    
}

