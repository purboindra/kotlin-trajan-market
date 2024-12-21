package com.example.trajanmarket.ui.screens.cart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.trajanmarket.data.model.Product
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.ui.components.CartItem

@Composable
fun CartScreen(
    cartViewModel: CartViewModel = hiltViewModel<CartViewModel>(),
    navHostController: NavHostController
) {
    
    val cartListState = cartViewModel.cartListState.collectAsState()
    
    LaunchedEffect(Unit) {
        cartViewModel.getCarts()
    }
    
    fun handleClickItem(id: Int) {
        navHostController.navigate("product_detail/${id}")
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            when (cartListState.value) {
                is State.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                
                is State.Succes -> {
                    val data = (cartListState.value as State.Succes<List<Product.Product>>).data
                    Box(modifier = Modifier.fillParentMaxSize()) {
                        LazyColumn {
                            items(data) { item ->
                                CartItem(item, onClick = {
                                    handleClickItem(item.id)
                                })
                            }
                        }
                    }
                }
                
                is State.Failure -> {
                    val throwable = (cartListState.value as State.Failure).throwable
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(throwable.message ?: "Unknown Error")
                    }
                }
                
                else -> {}
            }
        }
    }
}