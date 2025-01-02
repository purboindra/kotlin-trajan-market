package com.example.trajanmarket.data.repository

import android.util.Log
import com.example.trajanmarket.data.local.CartDao
import com.example.trajanmarket.data.local.datastore.UserPreferences
import com.example.trajanmarket.data.model.CartEntity
import com.example.trajanmarket.data.model.Product
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.data.remote.api.AddToCartParams
import com.example.trajanmarket.data.remote.api.CartApi
import com.example.trajanmarket.data.remote.api.ProductApi
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope


class CartRepository(
    private val cartApi: CartApi,
    private val productApi: ProductApi,
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
    
    fun getCarts(): Flow<State<List<Product.Product>>> = flow {
        emit(State.Loading)
        
        try {
            val userId = userPreferences.userId.firstOrNull()
            if (userId == null) {
                emit(State.Failure(IllegalStateException("User ID is null")))
                return@flow
            }
            
            val result = cartDao.getCartByUserId(userId.toInt())
            
            val products = coroutineScope {
                result.map { product ->
                    async { productApi.fetchProductById(product.productId.toString()) }
                }.awaitAll()
            }
            
            emit(State.Succes(products))
        } catch (e: Throwable) {
            Log.d("error getCartByUserId", e.message ?: "Unknown error")
            emit(State.Failure(e))
        }
    }
    
    fun getLocalCarts(): Flow<State<List<CartEntity>>> = flow {
        emit(State.Loading)
        
        try {
            val userId = userPreferences.userId.firstOrNull()
            if (userId == null) {
                emit(State.Failure(IllegalStateException("User ID is null")))
                return@flow
            }
            
            val result = cartDao.getCartByUserId(userId.toInt())
            
            emit(State.Succes(result))
        } catch (e: Throwable) {
            Log.d("error getCartByUserId", e.message ?: "Unknown error")
            emit(State.Failure(e))
        }
    }
    
    fun removeFromCart(productId: String): Flow<State<Boolean>> = flow {
        emit(State.Loading)
        try {
            delay(1000)
            val result = cartDao.removeFromCart(productId.toInt())
            if (result > 0) {
                emit(State.Succes(true))
            } else {
                emit(State.Failure(Throwable(message = "Failed delete product from cart")))
            }
        } catch (e: Throwable) {
            Log.d("error removeFromCart", e.message ?: "Unknown error")
            emit(State.Failure(e))
        }
    }
}