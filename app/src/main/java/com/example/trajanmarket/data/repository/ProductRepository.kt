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
    ): Flow<State<List<Product.Product>>> =
        flow {


            emit(State.Loading)
            try {
                val products = mutableListOf<Product.Product>()

                val documents = appwriteDatabase.listDocuments(
                    databaseId,
                    collectionProducts,
                )

                Log.d(TAG, "Documents products: $documents")

                if (documents.total.toInt() == 0) {
                    emit(State.Succes(products))
                    return@flow
                }

                for (document in documents.documents) {
                    val data = document.data
                    Log.d(TAG, "Data: $data")
                    val dataToString = Json.encodeToString(data)
                    Log.d(TAG,"data to string: $dataToString")
                    val product = Json.decodeFromString<Product.Product>(dataToString)

                    Log.d(TAG, "Decode product: $product")

                    products.add(product)
                }

                emit(State.Succes(products))

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
                        Query.equal("name", product.title)
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

