package com.example.trajanmarket.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trajanmarket.data.model.Categories
import com.example.trajanmarket.data.model.Product
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.domain.usecases.GetProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productUseCase: GetProductUseCase
) : ViewModel() {
    
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory
    
    private val _categories = MutableStateFlow<State<List<String>>>(State.Idle)
    val categories: StateFlow<State<List<String>>> = _categories
    
    private val _products = MutableStateFlow<State<List<Product.Product>>>(State.Idle)
    val products: StateFlow<State<List<Product.Product>>> = _products
    
    private val _productsByCategory = MutableStateFlow<State<Product>>(State.Idle)
    val productsByCategory: StateFlow<State<Product>> = _productsByCategory
    
    private fun fetchAllProducts(
        sortBy: String? = null,
        order: String? = null,
        limit: String? = null
    ) =
        viewModelScope.launch {
            val product = productUseCase.invoke(sortBy, order, limit)
            product.collectLatest { state ->
                _products.emit(state)
            }
        }
    
    fun onCategorySelect(category: String) {
        _selectedCategory.update { category }
        fetchProductsByCategory(category)
    }
    
    private fun fetchCategoryProductList() = viewModelScope.launch {
        val categories = productUseCase.fetchCategoryProductList()
        categories.collectLatest { state ->
            _categories.emit(state)
        }
    }
    
    private fun fetchProductsByCategory(category: String) = viewModelScope.launch {
        val products = productUseCase.fetchProductsByCategory(category)
        products.collectLatest { state ->
            _productsByCategory.emit(state)
        }
    }
    
    init {
        fetchCategoryProductList()
        fetchProductsByCategory("groceries")
        fetchAllProducts(
            sortBy = "order", order = "asc", limit = "10"
        )
    }
    
}