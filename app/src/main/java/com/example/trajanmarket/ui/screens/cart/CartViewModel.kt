package com.example.trajanmarket.ui.screens.cart

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import kotlinx.coroutines.flow.map
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

    fun addToCart(product: Product.Product) = viewModelScope.launch {
        cartUseCase.addToCart(product).collectLatest { state ->
            _addToCartState.value = state
            if (state is State.Succes) {
                cartUseCase.getCarts().collectLatest { stateGetCart ->
                    _cartListState.value = stateGetCart
                }
            }
        }
    }

    fun removeFromCart(productId: String) = viewModelScope.launch {
        cartUseCase.removeFromCart(productId).collectLatest { state ->
            _removeFromCartState.value = state
        }
    }


    fun checkProductInCart(id: String) {
        viewModelScope.launch {
            cartUseCase.getCarts()
                .map { cartListState ->
                    val isProductInCart =
                        (cartListState as? State.Succes)?.data?.any {
                            Log.d("checkProductInCart", "productId: ${it.id} == ${it.appwriteId}")
                            /// TODO
                            it.id == id
                        }
                    Log.d(
                        "checkProductInCart",
                        "cartListState: $cartListState, isProductInCart: $isProductInCart"
                    )
                    isProductInCart
                }
                .collect { isInCart ->
                    isInCart?.let {
                        Log.d(
                            "checkProductInCart",
                            "Updating state with: $isInCart for productId: $id"
                        )
                        /// TODO
//                        _hasProductInCart.value = it
                    } ?: run {
                        Log.d("checkProductInCart", "isInCart is null for productId: $id")
                    }
                }
        }
    }

    fun getCarts() = viewModelScope.launch {
        cartUseCase.getCarts().collectLatest { state ->
            when (state) {
                is State.Loading -> Log.d("CartViewModel", "Loading...")
                is State.Succes -> Log.d("CartViewModel", "Success: ${state.data}")
                is State.Failure -> Log.d("CartViewModel", "Failure: ${state.throwable.message}")
                else -> Log.d("CartViewModel", "Initialize")
            }

            if (state is State.Succes) {
                /// TODO
               checkProductInCart("1")
            }
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