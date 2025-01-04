package com.example.trajanmarket.domain.appwrite

import com.example.trajanmarket.BuildConfig
import io.appwrite.Client

private const val TAG = "AppwriteClient"

object AppwriteClient {
    
    const val API_KEY = BuildConfig.API_KEY
    const val PROJECT_ID = BuildConfig.PROJECT_ID
    
    const val DATABASE_ID = BuildConfig.DATABASE_ID
    
    // COLLECTIONS
    const val COLLECTION_USERS = BuildConfig.COLLECTION_USERS
    const val COLLECTION_PRODUCTS = BuildConfig.COLLECTION_PRODUCTS
    const val COLLECTION_CARTS = BuildConfig.COLLECTION_CARTS
    
    val client: Client by lazy {
        println("$TAG: $API_KEY $PROJECT_ID")
        Client()
            .setEndpoint("https://cloud.appwrite.io/v1")
            .setProject(PROJECT_ID)
    }
    
}