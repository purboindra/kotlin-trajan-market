package com.example.trajanmarket.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.appwrite.Client
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppwriteModule {
    @Provides
    @Singleton
    fun provideAppwriteClient(@ApplicationContext context: Context): Client {
        return Client(context).setProject("6775ff42000c0b501510")
    }
}