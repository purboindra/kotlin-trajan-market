package com.example.trajanmarket.ui.screens.cart

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trajanmarket.data.model.CartEntity
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
import kotlin.math.roundToInt

@HiltViewModel
class CartViewModel @Inject constructor(private val cartUseCase: CartUseCase) : ViewModel() {
    private val _addToCartState = MutableStateFlow<State<Boolean>>(State.Idle)
    val addToCartState: StateFlow<State<Boolean>> = _addToCartState
    
    private val _removeFromCartState = MutableStateFlow<State<Boolean>>(State.Idle)
    val removeFromCartState: StateFlow<State<Boolean>> = _removeFromCartState
    
    private val _cartListState =
        MutableStateFlow<State<List<Product.Product>>>(State.Succes(emptyList()))
    val cartListState: StateFlow<State<List<Product.Product>>> = _cartListState
    
    private val _cartLocalListState =
        MutableStateFlow<State<List<CartEntity>>>(State.Succes(emptyList()))
    val cartLocalListState: StateFlow<State<List<CartEntity>>> = _cartLocalListState
    
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
    
    fun getLocalCarts() = viewModelScope.launch {
        cartUseCase.getLocalCarts().collectLatest { state ->
            _cartLocalListState.value = state
        }
    }
    
    fun getCarts() = viewModelScope.launch {
        cartUseCase.getCarts().collectLatest { state ->
            _cartListState.value = state
        }
    }
    
    @SuppressLint("DefaultLocale")
     fun getOriginalPrice(discountPercentage: Double, discountPrice: Double): Int? {
        if (discountPercentage >= 100.0 || discountPercentage < 0) {
            Log.e("getRealPrice", "Invalid discount percentage: $discountPercentage")
            return null
        }
        val realPrice = discountPrice / (1 - (discountPercentage / 100))
        val formattedNumber = String.format("%.2f", realPrice).toDouble()
        return formattedNumber.roundToInt()
    }
    
}