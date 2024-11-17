package com.example.trajanmarket.ui.screens.product.detail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trajanmarket.ui.screens.product.ProductViewModel

@Composable
fun ProductDetail(
    productViewModel: ProductViewModel = hiltViewModel(),
    id: String
) {
    
    val productByIdState by productViewModel.productByIdState.collectAsState()
    
    LaunchedEffect(Unit) {
        productViewModel.fetchProductById(id)
    }
    
    Scaffold(
        modifier = Modifier
            .safeContentPadding()
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 12.dp)
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    Text("Hello")
                }
            }
        }
    }
}