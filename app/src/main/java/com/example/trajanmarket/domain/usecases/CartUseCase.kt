package com.example.trajanmarket.domain.usecases

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.trajanmarket.data.model.CartEntity
import com.example.trajanmarket.data.model.Product
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.data.remote.api.AddToCartParams
import com.example.trajanmarket.data.repository.CartRepository
import io.appwrite.services.Databases
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartUseCase @Inject constructor(
    private val cartRepository: CartRepository,
) {
    fun addToCart(product: Product.Product): Flow<State<Boolean>> =
        cartRepository.addToCart(product)

    fun getCarts(): Flow<State<List<Product.Product>>> = cartRepository.getCarts()

    fun removeFromCart(productId: String): Flow<State<Boolean>> =
        cartRepository.removeFromCart(productId)

    fun checkProductInCart(productId: String): Flow<State<Boolean>> =
        cartRepository.checkProductInCart(productId)
}