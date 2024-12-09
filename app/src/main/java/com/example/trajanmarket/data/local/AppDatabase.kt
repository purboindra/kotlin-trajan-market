package com.example.trajanmarket.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.trajanmarket.data.model.CartEntity
import com.example.trajanmarket.data.model.UserEntity

@Database(entities = [UserEntity::class, CartEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun cartDao(): CartDao
}