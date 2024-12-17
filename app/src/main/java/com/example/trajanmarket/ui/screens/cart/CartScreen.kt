package com.example.trajanmarket.ui.screens.cart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.trajanmarket.ui.screens.viewmodel.CartViewModel

@Composable
fun CartScreen(
    cartViewModel: CartViewModel = hiltViewModel<CartViewModel>(),
    navHostController: NavHostController
) {
    
    val cartListState = cartViewModel.cartListState.collectAsState()
    
    LaunchedEffect(Unit) {
        cartViewModel.getCarts()
    }
    
    LazyColumn(modifier = Modifier.fillMaxSize().safeContentPadding()) {
        item {
            Box(modifier = Modifier.fillMaxHeight()){
                Text("Hello World")
            }
        }
    }
    
}