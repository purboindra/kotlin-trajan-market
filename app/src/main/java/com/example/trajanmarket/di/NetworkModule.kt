package com.example.trajanmarket.di

import com.example.trajanmarket.data.local.CartDao
import com.example.trajanmarket.data.local.datastore.UserPreferences
import com.example.trajanmarket.data.remote.api.AuthApi
import com.example.trajanmarket.data.remote.api.CartApi
import com.example.trajanmarket.data.remote.api.ProductApi
import com.example.trajanmarket.data.remote.service.HttpClientProvider
import com.example.trajanmarket.data.repository.AuthRepository
import com.example.trajanmarket.data.repository.CartRepository
import com.example.trajanmarket.data.repository.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.appwrite.services.Account
import io.appwrite.services.Databases
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = HttpClientProvider.client
    
    @Provides
    @Singleton
    fun provideProductApi(client: HttpClient): ProductApi = ProductApi(client)
    
    @Provides
    @Singleton
    fun provideCartApi(client: HttpClient): CartApi = CartApi(client)
    
    @Provides
    @Singleton
    fun provideProductRepository(api: ProductApi, appwriteDatabases: Databases) =
        ProductRepository(api, appwriteDatabases)
    
    @Provides
    @Singleton
    fun provideCartRepository(
        productApi: ProductApi,
        userPreferences: UserPreferences,
        cartDao: CartDao,
        appwriteDatabases: Databases,
    ) =
        CartRepository(appwriteDatabase = appwriteDatabases, productApi, userPreferences, cartDao)
    
    // AUTH
    @Provides
    @Singleton
    fun provideAuthApi(client: HttpClient): AuthApi = AuthApi(client)
    
    @Provides
    @Singleton
    fun provideAuthRepository(
        database: Databases,
        account: Account,
        userPreferences: UserPreferences
    ): AuthRepository =
        AuthRepository(database, account, userPreferences)
}