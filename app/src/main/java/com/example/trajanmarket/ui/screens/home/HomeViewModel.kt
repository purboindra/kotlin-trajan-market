package com.example.trajanmarket.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trajanmarket.data.model.Product
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.domain.usecases.GetProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productUseCase: GetProductUseCase
) : ViewModel() {
    
    private val _products = MutableStateFlow<State<Product>>(State.Idle)
    val products: StateFlow<State<Product>> = _products
    
    fun fetchAllProducts() = viewModelScope.launch {
        val product = productUseCase.invoke()
        product.collectLatest { state ->
            _products.emit(state)
        }
    }
    
}