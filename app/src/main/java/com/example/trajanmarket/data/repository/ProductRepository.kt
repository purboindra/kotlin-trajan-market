package com.example.trajanmarket.data.repository

import com.example.trajanmarket.data.model.Categories
import com.example.trajanmarket.data.model.Product
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.data.remote.api.ProductApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductRepository(private val api: ProductApi) {
    suspend fun fetchAllProducts(): Flow<State<Product>> = flow {
        emit(State.Loading)
        try {
            val product = api.fetchAllProducts()
            emit(State.Succes(product))
        } catch (e: Exception) {
            emit(State.Failure(e))
        }
    }
    
    suspend fun fetchProductsByCategory(category: String) = flow {
        emit(State.Loading)
        try {
            val product = api.fetchProductsByCategory(category)
            emit(State.Succes(product))
        } catch (e: Exception) {
            emit(State.Failure(e))
        }
    }
    
    suspend fun fetchCategoryProductList() = flow {
        emit(State.Loading)
        try {
            val categories = api.fetchCategoryProductList()
            emit(State.Succes(categories))
        } catch (e: Exception) {
            emit(State.Failure(e))
        }
    }
}

