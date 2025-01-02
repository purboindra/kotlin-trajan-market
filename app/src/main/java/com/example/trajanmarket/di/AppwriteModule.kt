package com.example.trajanmarket.di

import com.example.trajanmarket.domain.appwrite.AppwriteClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.appwrite.Client
import io.appwrite.services.Account
import io.appwrite.services.Databases
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppwriteModule {
    @Provides
    @Singleton
    fun provideAppwriteClient(): Client {
        return AppwriteClient.client
    }
    
    @Provides
    @Singleton
    fun provideAppwriteAccount(): Account {
        return Account(AppwriteClient.client)
    }
    
    @Provides
    @Singleton
    fun provideAppwriteDatabase(): Databases {
        return Databases(AppwriteClient.client)
    }
}