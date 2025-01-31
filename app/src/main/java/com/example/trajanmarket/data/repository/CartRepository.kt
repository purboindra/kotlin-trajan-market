package com.example.trajanmarket.data.repository

import android.util.Log
import com.example.trajanmarket.data.local.CartDao
import com.example.trajanmarket.data.local.datastore.UserPreferences
import com.example.trajanmarket.data.model.CartEntity
import com.example.trajanmarket.data.model.Product
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.data.remote.api.ProductApi
import com.example.trajanmarket.domain.appwrite.AppwriteClient
import io.appwrite.ID
import io.appwrite.Query
import io.appwrite.services.Databases
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private const val TAG = "CartRepository"

class CartRepository(
    private val appwriteDatabase: Databases,
    private val productApi: ProductApi,
    private val userPreferences: UserPreferences,
    private val cartDao: CartDao
) {

    private val databaseId = AppwriteClient.DATABASE_ID
    private val collectionCarts = AppwriteClient.COLLECTION_CARTS

    fun addToCart(product: Product.Product): Flow<State<Boolean>> = flow {

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
                    Query.equal("name", product.title)
                )
            )

            val productId = productsAppwrite.documents.first().id
            val uniqueId = ID.unique()

            val dataCart = mapOf(
                "id" to uniqueId,
                "quantity" to "1",
                "created_at" to createdAt,
                "price" to product.price,
                "products" to productId,
                "users" to userId,
            )

            appwriteDatabase.createDocument(
                databaseId,
                collectionId = collectionCarts,
                documentId = uniqueId,
                dataCart,
            )

            val cartEntity = CartEntity(
                productId = product.id.toInt(),
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

        val products = mutableListOf<Product.Product>()

        try {
            val userId = userPreferences.userId.firstOrNull()
            if (userId == null) {
                emit(State.Failure(IllegalStateException("User ID is null")))
                return@flow
            }

            val appwriteProductList = appwriteDatabase.listDocuments(
                databaseId,
                collectionCarts,
                queries = listOf(
                    Query.equal(
                        "users", userId
                    )
                )
            )

            val appwriteProducts = appwriteProductList.documents.map {

                val data = it.data["products"] as Map<*, *>

                val product = Product.Product(
                    id = data["id"] as String,
                    title = data["name"] as String,
                    description = data["description"] as String,
                    price = data["price"] as String,
                    thumbnail = data["thumbnail"] as String,
                    sku = data["sku"] as String,
                    stock = data["stock_quantity"] as String,
                    availabilityStatus = "",
                    weight = data["weight"] as String,
                    rating = data["average_rating"] as Double,
                    createdAt = "",
                    images = data["images"] as List<String>,
                    tags = data["tags"] as List<String>,
                )
                product
            }

            products.addAll(appwriteProducts)

            emit(State.Succes(products))
        } catch (e: Throwable) {
            Log.d("error getCarts", e.message ?: "Unknown error")
            e.printStackTrace()
            emit(State.Failure(e))
        }
    }

    fun removeFromCart(productId: String): Flow<State<Boolean>> = channelFlow {
        send(State.Loading)
        try {

            val products = appwriteDatabase.listDocuments(
                databaseId,
                collectionCarts,
                queries = listOf(
                    Query.equal("products", productId)
                )
            )

            if (products.documents.isNotEmpty()) {
                appwriteDatabase.deleteDocument(
                    databaseId,
                    collectionCarts,
                    products
                        .documents.first().id
                )

                checkProductInCart(productId).collectLatest { state ->
                    send(State.Succes(true))
                }
            } else {
                send(State.Failure(Throwable(message = "Failed delete product from cart")))
            }

        } catch (e: Throwable) {
            Log.d("error removeFromCart", e.message ?: "Unknown error")
            send(State.Failure(e))
        } finally {
            close()
        }
    }

    fun checkProductInCart(productId: String): Flow<State<Boolean>> = flow {

        emit(State.Loading)

        try {
            val product = appwriteDatabase.listDocuments(
                databaseId,
                collectionCarts,
                queries = listOf(
                    Query.equal("products", productId)
                )
            )

            val hasInCart = product.documents.isNotEmpty()

            if (hasInCart) {
                emit(State.Succes(true))
            } else {
                emit(State.Succes(false))
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error check product in cart: ${e.message}")
            emit(State.Failure(e))
        }
    }
}