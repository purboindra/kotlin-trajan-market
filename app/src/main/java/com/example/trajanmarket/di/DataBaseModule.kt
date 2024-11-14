package com.example.trajanmarket.di

import android.content.Context
import androidx.room.Room
import com.example.trajanmarket.data.local.AppDatabase
import com.example.trajanmarket.data.local.UserDao
import com.example.trajanmarket.data.local.datastore.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()
    }
    
    @Provides
    fun providerUserDao(db: AppDatabase): UserDao {
        return db.userDao()
    }
    
    // LOCAL STORAGE OR DATA STORE
    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }
}