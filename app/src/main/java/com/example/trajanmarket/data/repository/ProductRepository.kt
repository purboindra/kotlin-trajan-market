package com.example.trajanmarket.data.repository

import android.util.Log
import com.example.trajanmarket.data.model.Product
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.data.remote.api.ProductApi
import com.example.trajanmarket.domain.appwrite.AppwriteClient
import io.appwrite.ID
import io.appwrite.Query
import io.appwrite.services.Databases
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private const val TAG = "ProductRepository"

class ProductRepository(private val api: ProductApi, private val appwriteDatabase: Databases) {

    private val databaseId = AppwriteClient.DATABASE_ID
    private val collectionProducts = AppwriteClient.COLLECTION_PRODUCTS

    fun fetchAllProducts(
        sortBy: String?,
        order: String?,
        limit: String?
    ): Flow<State<Product>> =
        flow {
            emit(State.Loading)
            try {
                val product = api.fetchAllProducts(
                    sortBy,
                    order, limit
                )

//                val products = product.products

//                val enrichedProducts = coroutineScope {
//                    products.map { product ->
//                        async {
//                            val appwriteId = ID.unique()
//                            val enrichedProduct = product.copy(appwriteId = appwriteId)
//
//                            try {
//                                storeProductToAppwriteDatabase(enrichedProduct)
//                            } catch (e: Exception) {
//                                Log.e(TAG, "Error storing product: ${e.message}")
//                            }
//
//                            enrichedProduct
//                        }
//                    }.awaitAll()
//                }
//
//                val enrichedProduct = product.copy(
//                    products = enrichedProducts
//                )

//                val appwriteProduct = appwriteDatabase.listDocuments(
//                    databaseId,
//                    collectionProducts
//                )
//
//                val appwriteProductList = appwriteProduct.documents
//
//                val appwriteProducts = appwriteProductList.map {
//                    Product.Product(
//                        id = it.data["id"] as String,
//                        title = it.data["name"] as String,
//                        description = it.data["description"] as String,
//                        price = it.data["price"] as String,
//                        thumbnail = it.data["thumbnail"] as String,
//                        sku = it.data["sku"] as String,
//                        stock = it.data["stock_quantity"] as String,
//                        availabilityStatus = "",
//                        weight = it.data["weight"] as String,
//                        rating = it.data["average_rating"] as Double,
//                        createdAt = it.data["created_at"] as String,
//                        images = it.data["images"] as List<String>,
//                        tags = it.data["tags"] as List<String>,
//                    )
//                }


                emit(State.Succes(product))
            } catch (e: Exception) {
                Log.d(TAG, "Error fetch all products: ${e.message}")

                e.printStackTrace()
                emit(State.Failure(e))
            }
        }

    fun fetchProductsByCategory(category: String) = flow {
        emit(State.Loading)
        try {
            val product = api.fetchProductsByCategory(category)

//            val products = product.products
//
//            val enrichedProducts = products.map {
//                val id = ID.unique()
//                val enrichedProduct = it.copy(
//                    appwriteId = id
//                )
//                enrichedProduct
//            }
//
//            val enrichedProduct = product.copy(
//                products = enrichedProducts
//            )

            emit(State.Succes(product))
        } catch (e: Exception) {

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

    fun fetchProductById(id: String) = flow {
        emit(State.Loading)
        try {
            val product = api.fetchProductById(id)
            val appwriteProduct = try {
                storeProductToAppwriteDatabase(product)
            } catch (e: Exception) {
                Log.d(TAG, "Error storing product to Appwrite Database: ${e.message}")
                throw Exception("Error storing product to Appwrite Database: ${e.message}")
            }

            val enrichedProduct = product.copy(
                appwriteId = appwriteProduct["id"] as String
            )

            Log.d(TAG, "Enriched product by id: $enrichedProduct")

            emit(State.Succes(enrichedProduct))
        } catch (e: Exception) {
            emit(State.Failure(e))
        }
    }

    fun searchProduct(query: String?) = flow {
        emit(State.Loading)
        Log.d("Query", query ?: "None")
        try {
            val product = api.searchProduct(query)
            emit(State.Succes(product))
        } catch (e: Exception) {
            emit(State.Failure(e))
        }
    }

    private suspend fun storeProductToAppwriteDatabase(product: Product.Product): Map<String, Any> =
        withContext(Dispatchers.IO) {
            val createdAt = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

            val id = ID.unique()

            val data = mapOf(
                "id" to id,
                "name" to product.title,
                "description" to product.description,
                "price" to product.price,
                "thumbnail" to product.thumbnail,
                "sku" to product.sku,
                "stock_quantity" to product.stock,
                "is_available" to true,
                "weight" to product.weight,
                "average_rating" to product.rating,
                "created_at" to createdAt,
                "images" to product.images,
                "tags" to product.tags,
            )

            try {
                val products = appwriteDatabase.listDocuments(
                    databaseId,
                    collectionProducts,
                    listOf(
                        Query.equal("name", product.title ?: "")
                    )
                )

                val isProductExist = products.documents.isNotEmpty()

                if (isProductExist) {
                    return@withContext products.documents.first().data
                }

                appwriteDatabase.createDocument(
                    databaseId,
                    collectionId = collectionProducts,
                    documentId = id,
                    data = data,
                )

                throw Exception("Product not found")

            } catch (e: Exception) {
                Log.d(TAG, "Error storing product: ${e.message}")
                throw e
            }
        }
}

