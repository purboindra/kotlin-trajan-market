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

                val products = product.products

                val enrichedProducts = coroutineScope {
                    products.map { product ->
                        async {
                            val appwriteId = ID.unique()
                            val enrichedProduct = product.copy(appwriteId = appwriteId)

                            try {
                                storeProductToAppwriteDatabase(enrichedProduct)
                            } catch (e: Exception) {
                                Log.e(TAG, "Error storing product: ${e.message}")
                            }

                            enrichedProduct
                        }
                    }.awaitAll()
                }

                val enrichedProduct = product.copy(
                    products = enrichedProducts
                )


                val appwriteProduct = appwriteDatabase.listDocuments(
                    databaseId,
                    collectionProducts
                )

                val appwriteProductList = appwriteProduct.documents

                for(appwriteProductTest in appwriteProductList){
                    Log.d("TEST","Product test: ${appwriteProductTest.data}")
                    val encode = Json.encodeToString(appwriteProductTest.data)
                    Log.d("TEST","Product test: $encode")
                }

                val appwriteProducts = appwriteProductList.map {
                    Log.d("Product", it.data.toString())
                    val dataToString = Json.encodeToString(it.data)
                    Log.d("Product", "Data to string: $dataToString")
                    Json.decodeFromString<Product>(dataToString)
                }

                Log.d("Product", "appwrite products: $appwriteProducts")

                emit(State.Succes(enrichedProduct))
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
            try {
                storeProductToAppwriteDatabase(product)
            } catch (e: Exception) {
                Log.d(TAG, "Error storing product to Appwrite Database: ${e.message}")
            }

            emit(State.Succes(product))
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

    private suspend fun storeProductToAppwriteDatabase(product: Product.Product) =
        withContext(Dispatchers.IO) {
            val createdAt = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)

            val id = ID.unique()

            val data = mapOf(
                "id" to id,
                "name" to product.title,
                "description" to product.description,
                "price" to product.price.toString(),
                "thumbnail" to product.thumbnail,
                "sku" to product.sku,
                "stock_quantity" to product.stock.toString(),
                "is_available" to true,
                "weight" to product.weight.toString(),
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
                    throw Exception("Product already exists in Appwrite Database.")
                }

                appwriteDatabase.createDocument(
                    databaseId,
                    collectionId = collectionProducts,
                    documentId = id,
                    data = data,
                )
            } catch (e: Exception) {
                Log.d(TAG, "Error storing product: ${e.message}")
                throw e
            }
        }
}

