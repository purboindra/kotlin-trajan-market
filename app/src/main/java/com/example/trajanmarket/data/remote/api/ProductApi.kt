package com.example.trajanmarket.data.remote.api

import com.example.trajanmarket.data.model.Categories
import com.example.trajanmarket.data.model.Product
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ProductApi(private val client: HttpClient) {
    suspend fun fetchAllProducts(
        sortBy: String?,
        order: String? = null,
        limit: String? = null
    ): Product =
        client.get("products") {
            url {
                sortBy?.let {
                    parameters.append("sortBy", it)
                }
                order?.let {
                    parameters.append("order", it)
                }
                limit?.let {
                    parameters.append("limit", it)
                }
            }
        }.body()
    
    suspend fun fetchProductsByCategory(category: String): Product =
        client.get("products/category/${category}").body()
    
    suspend fun fetchCategoryProductList(): List<String> =
        client.get("products/category-list").body()
}