package com.example.trajanmarket.ui.screens.product

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trajanmarket.data.model.Product
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.data.remote.api.AddToCartParams
import com.example.trajanmarket.domain.usecases.CartUseCase
import com.example.trajanmarket.domain.usecases.GetProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val cartUseCase: CartUseCase,
) : ViewModel() {
    private val _productState = MutableStateFlow<State<Product>>(State.Idle)
    var productState: StateFlow<State<Product>> = _productState

    private val _price = MutableStateFlow(0.0)
    val price: StateFlow<Double> = _price

    private val _productByIdState = MutableStateFlow<State<Product.Product>>(State.Idle)
    val productByIdState: StateFlow<State<Product.Product>> = _productByIdState

    private val _hasProductInCart = MutableStateFlow<State<Boolean>>(State.Idle)
    val hasProductInCart = _hasProductInCart.asStateFlow()

    @SuppressLint("DefaultLocale")
    private fun getOriginalPrice(discountPercentage: Double, discountPrice: Double) {
        if (discountPercentage >= 100.0 || discountPercentage < 0) {
            Log.e("getRealPrice", "Invalid discount percentage: $discountPercentage")
            return
        }
        val realPrice = discountPrice / (1 - (discountPercentage / 100))
        val formattedNumber = String.format("%.2f", realPrice).toDouble()
        _price.update { formattedNumber }
    }

    fun fetchProductById(id: String) = viewModelScope.launch {
        getProductUseCase.fetchProductById(id).collectLatest { state ->
            _productByIdState.emit(state)
            if (state is State.Succes) {
                val product = state.data
                getOriginalPrice(
                    discountPercentage = product.discountPercentage,
                    discountPrice = product.price.toDouble()
                )

                product.appwriteId?.let {
                    cartUseCase.checkProductInCart(product.appwriteId)
                        .collectLatest { stateCheckProductInCart ->
                            _hasProductInCart.emit(stateCheckProductInCart)
                        }
                }
            }
        }
    }
}