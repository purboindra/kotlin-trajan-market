package com.example.trajanmarket.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trajanmarket.data.model.CartEntity

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cartEntity: CartEntity)
    
    @Query("SELECT * from cart_table WHERE userId = :userId")
    suspend fun getCartByUserId(userId: Int): List<CartEntity>
    
    @Query("SELECT * from cart_table WHERE productId = :productId")
    suspend fun getCartByProductId(productId: Int): CartEntity?
    
    @Query("SELECT * FROM cart_table WHERE id = :cartId")
    suspend fun getCartByCartId(cartId: Int): CartEntity?
}