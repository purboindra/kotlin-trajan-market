package com.example.trajanmarket.domain.usecases

import com.example.trajanmarket.data.model.CartEntity
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.data.remote.api.AddToCartParams
import com.example.trajanmarket.data.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartUseCase @Inject constructor(private val cartRepository: CartRepository) {
    fun addToCart(products: List<AddToCartParams>): Flow<State<Boolean>> =
        cartRepository.addToCart(products)
    
    fun getCarts(): Flow<State<List<CartEntity>>> = cartRepository.getCarts()
}