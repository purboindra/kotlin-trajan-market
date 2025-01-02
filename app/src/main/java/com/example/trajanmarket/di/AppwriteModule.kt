package com.example.trajanmarket.di

import com.example.trajanmarket.domain.appwrite.AppwriteClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.appwrite.Client
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppwriteModule {
    @Provides
    @Singleton
    fun provideAppwriteClient(): Client {
        return AppwriteClient.client
    }
}