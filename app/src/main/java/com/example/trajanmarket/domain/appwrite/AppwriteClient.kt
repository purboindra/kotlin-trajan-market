package com.example.trajanmarket.domain.appwrite

import com.example.trajanmarket.BuildConfig
import io.appwrite.Client

object AppwriteClient {
    
    private const val API_KEY = BuildConfig.API_KEY
    private const val PROJECT_ID = BuildConfig.PROJECT_ID
    
    val client: Client by lazy {
        Client()
            .setEndpoint("https://cloud.appwrite.io/v1")
            .setProject(PROJECT_ID)
            .setKey(API_KEY)
    }
    
}