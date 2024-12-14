package com.example.trajanmarket.data.repository

import android.util.Log
import com.example.trajanmarket.data.local.CartDao
import com.example.trajanmarket.data.local.datastore.UserPreferences
import com.example.trajanmarket.data.model.CartEntity
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.data.remote.api.AddToCartParams
import com.example.trajanmarket.data.remote.api.CartApi
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json


class CartRepository(
    private val cartApi: CartApi,
    private val userPreferences: UserPreferences,
    private val cartDao: CartDao
) {
    
    fun addToCart(products: List<AddToCartParams>): Flow<State<Boolean>> = flow {
        
        Log.d("addToCart", "Add to cart called: $products")
        
        emit(State.Loading)
        try {
            val userId = userPreferences.userId.first() ?: throw Exception("User not valid")
            
            val response = cartApi.addToCart(products, userId)
            
            val cartEntity = CartEntity(
                productId = products[0].id.toInt(),
                userId = userId.toInt(),
                quantity = 1
            )
            
            if (response.status.isSuccess()) {
                cartDao.insert(cartEntity)
                emit(State.Succes(true))
            } else {
                val errorText = response.bodyAsText()
                val errorResponse = try {
                    Json.decodeFromString<ErrorResponse>(errorText)
                } catch (e: Throwable) {
                    ErrorResponse(e.message ?: "Unknown error occurred")
                }
                emit(State.Failure(Exception(errorResponse.message)))
            }
        } catch (e: Throwable) {
            Log.d("error add to cart", e.message ?: "Unknown error")
            emit(State.Failure(e))
        }
    }
    
    fun getCarts(): Flow<State<List<CartEntity>>> = flow {
        Log.d("getCarts", "Get carts cartRepository called")
        
        try {
            val userId = userPreferences.userId.first()
            userId?.let {
                val result = cartDao.getCartByUserId(it.toInt())
                emit(State.Succes(result))
            }
        } catch (e: Throwable) {
            Log.d("error getCartByUserId", e.message ?: "Unknown error")
            emit(State.Failure(e))
        }
    }
    
}