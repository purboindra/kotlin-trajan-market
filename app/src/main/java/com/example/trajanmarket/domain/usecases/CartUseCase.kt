package com.example.trajanmarket.domain.usecases

import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.data.repository.CartRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CartUseCase @Inject constructor(private val cartRepository: CartRepository) {
    fun addToCart(products: List<Map<String, Any>>) = flow<State<Boolean>> {
        cartRepository.addToCart(products)
    }
}