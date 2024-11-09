package com.example.trajanmarket.domain.usecases

import com.example.trajanmarket.data.model.Product
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.data.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductUseCase @Inject constructor (private val productRepository: ProductRepository)  {
    suspend operator fun invoke(): Flow<State<Product>> = productRepository.fetchAllProducts()
}