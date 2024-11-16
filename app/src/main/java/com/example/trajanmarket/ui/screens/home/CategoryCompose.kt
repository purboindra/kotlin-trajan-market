package com.example.trajanmarket.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.trajanmarket.data.model.Product
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.ui.components.LoadingShimmer
import com.example.trajanmarket.ui.components.ProductCategoryCardCompose
import com.example.trajanmarket.ui.theme.grayLight
import com.example.trajanmarket.utils.VerticalSpacer

@Composable
fun CategoryCompose(homeViewModel: HomeViewModel) {
    
    val productsState by homeViewModel.productsByCategory.collectAsState()
    
    LaunchedEffect(Unit) {
        homeViewModel.fetchProductsByCategory("groceries")
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text("Browse by Category", fontSize = 18.sp, fontWeight = FontWeight.W700)
        }
        10.VerticalSpacer()
        
        10.VerticalSpacer()
        Box(modifier = Modifier.height(180.dp)) {
            when (productsState) {
                is State.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        LoadingShimmer(150.dp)
                    }
                }
                
                is State.Succes -> {
                    val product = (productsState as State.Succes<Product>).data
                    val products = product.products
                    LazyRow(modifier = Modifier.padding(horizontal = 8.dp)) {
                        items(products) { product ->
                            ProductCategoryCardCompose(product)
                        }
                    }
                }
                
                is State.Failure -> {
                    val throwable = (productsState as State.Failure).throwable
                    Box(contentAlignment = Alignment.Center) {
                        Text(throwable.message ?: "Unknown Error")
                    }
                    
                }
                
                else -> {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}