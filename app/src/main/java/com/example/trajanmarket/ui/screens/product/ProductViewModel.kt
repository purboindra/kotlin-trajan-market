package com.example.trajanmarket.ui.screens.product

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trajanmarket.data.model.Product
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.domain.usecases.GetProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase
) : ViewModel() {
    private val _productState = MutableStateFlow<State<Product>>(State.Idle)
    var productState: StateFlow<State<Product>> = _productState
    
    private val _productByIdState = MutableStateFlow<State<Product.Product>>(State.Idle)
    val productByIdState: StateFlow<State<Product.Product>> = _productByIdState
    
    fun fetchProductById(id: String) = viewModelScope.launch {
        getProductUseCase.fetchProductById(id).collectLatest { state ->
            _productByIdState.emit(state)
        }
    }
}