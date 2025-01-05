package com.example.trajanmarket.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_table")
data class CartEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "userId") val userId: String?,
    @ColumnInfo(name = "productId") val productId: Int?,
    @ColumnInfo(name = "quantity") val quantity: Int?,
)