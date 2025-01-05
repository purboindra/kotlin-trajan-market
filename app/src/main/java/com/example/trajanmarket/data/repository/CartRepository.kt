package com.example.trajanmarket.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.trajanmarket.data.local.CartDao
import com.example.trajanmarket.data.local.datastore.UserPreferences
import com.example.trajanmarket.data.model.CartEntity
import com.example.trajanmarket.data.model.Product
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.data.remote.api.AddToCartParams
import com.example.trajanmarket.data.remote.api.CartApi
import com.example.trajanmarket.data.remote.api.ProductApi
import com.example.trajanmarket.domain.appwrite.AppwriteClient
import io.appwrite.ID
import io.appwrite.Query
import io.appwrite.services.Databases
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
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CartRepository(
    private val appwriteDatabase: Databases,
    private val productApi: ProductApi,
    private val userPreferences: UserPreferences,
    private val cartDao: CartDao
) {
    
    private val databaseId = AppwriteClient.DATABASE_ID
    private val collectionCarts = AppwriteClient.COLLECTION_CARTS
    
    @RequiresApi(Build.VERSION_CODES.O)
    fun addToCart(products: List<AddToCartParams>): Flow<State<Boolean>> = flow {
        
        emit(State.Loading)
        
        val createdAt = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        
        val userId = userPreferences.userId.firstOrNull()
        
        try {
            
            if (userId == null) {
                throw Exception("User ID not valid!")
            }
            
            // TODO REMOVE THIS LOGIC
            val productsAppwrite = appwriteDatabase.listDocuments(
                databaseId,
                AppwriteClient.COLLECTION_PRODUCTS,
                listOf(
                    Query.equal("name", products[0].name)
                )
            )
            
            val productId = productsAppwrite.documents[0].id
            
            // TODO GET PRODUCT ID FROM PARAMATER
            val dataCart = mapOf(
                "id" to ID.unique(),
                "quantity" to "1",
                "created_at" to createdAt,
                "price" to products[0].price.toString(),
                "products" to productId,
                "users" to userId,
            )
            
            appwriteDatabase.createDocument(
                databaseId,
                collectionId = collectionCarts,
                documentId = ID.unique(),
                dataCart,
            )
            
            val cartEntity = CartEntity(
                productId = products[0].id.toInt(),
                userId = userId,
                quantity = 1
            )
            
            cartDao.insert(cartEntity)
            
            emit(State.Succes(true))
            
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