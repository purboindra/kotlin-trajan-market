package com.example.trajanmarket.ui.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trajanmarket.data.model.Product
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.data.remote.api.AddToCartParams
import com.example.trajanmarket.domain.usecases.CartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val cartUseCase: CartUseCase) : ViewModel() {
    private val _addToCartState = MutableStateFlow<State<Boolean>>(State.Idle)
    val addToCartState: StateFlow<State<Boolean>> = _addToCartState
    
    private val _removeFromCartState = MutableStateFlow<State<Boolean>>(State.Idle)
    val removeFromCartState: StateFlow<State<Boolean>> = _removeFromCartState
    
    private val _cartListState =
        MutableStateFlow<State<List<Product.Product>>>(State.Succes(emptyList()))
    val cartListState: StateFlow<State<List<Product.Product>>> = _cartListState
    
    fun addToCart(products: List<AddToCartParams>) = viewModelScope.launch {
        cartUseCase.addToCart(products).collectLatest { state ->
            _addToCartState.value = state
        }
    }
    
    fun removeFromCart(productId: String) = viewModelScope.launch {
        cartUseCase.removeFromCart(productId).collectLatest { state ->
            _removeFromCartState.value = state
        }
    }
    
    fun getCarts() = viewModelScope.launch {
        cartUseCase.getCarts().collectLatest { state ->
            _cartListState.value = state
        }
    }
    
}