package com.example.trajanmarket.data.repository

import android.util.Log
import com.example.trajanmarket.data.model.Categories
import com.example.trajanmarket.data.model.Product
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.data.remote.api.ProductApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductRepository(private val api: ProductApi) {
    fun fetchAllProducts(sortBy: String?, order: String?, limit: String?): Flow<State<Product>> =
        flow {
            emit(State.Loading)
            try {
                val product = api.fetchAllProducts(sortBy, order, limit)
                emit(State.Succes(product))
            } catch (e: Exception) {
                emit(State.Failure(e))
            }
        }
    
    fun fetchProductsByCategory(category: String) = flow {
        emit(State.Loading)
        try {
            val product = api.fetchProductsByCategory(category)
            emit(State.Succes(product))
        } catch (e: Exception) {
            Log.e(
                "fetchProductsByCategory Repository",
                "An error occurred while fetching products by category",
                e
            )
            
            Log.e("fetchProductsByCategory Message", "Error Message: ${e.message}")
            Log.e("fetchProductsByCategory Cause", "Cause: ${e.cause}")
            Log.e("fetchProductsByCategory Stack Trace", Log.getStackTraceString(e))
            emit(State.Failure(e))
        }
    }
    
    fun fetchCategoryProductList() = flow {
        emit(State.Loading)
        try {
            val categories = api.fetchCategoryProductList()
            emit(State.Succes(categories))
        } catch (e: Exception) {
            emit(State.Failure(e))
        }
    }
}

