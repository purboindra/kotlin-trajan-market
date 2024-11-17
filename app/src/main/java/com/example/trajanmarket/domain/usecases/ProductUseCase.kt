package com.example.trajanmarket.domain.usecases

import com.example.trajanmarket.data.model.Product
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.data.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductUseCase @Inject constructor(private val productRepository: ProductRepository) {
    operator fun invoke(
        sortBy: String?,
        order: String?,
        limit: String?
    ): Flow<State<Product>> =
        productRepository.fetchAllProducts(sortBy, order, limit)
    
    fun fetchProductsByCategory(category: String) =
        productRepository.fetchProductsByCategory(category)
    
    fun fetchCategoryProductList() = productRepository.fetchCategoryProductList()
}