package com.example.trajanmarket.ui.screens.home

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.trajanmarket.data.model.Product
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.ui.components.LoadingCard
import com.example.trajanmarket.ui.components.ProductCategoryCardCompose
import com.example.trajanmarket.utils.VerticalSpacer

@Composable
fun CategoryCompose(homeViewModel: HomeViewModel, navHostController: NavHostController) {
    
    val productsState by homeViewModel.productsByCategory.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text("Browse by Category", fontSize = 18.sp, fontWeight = FontWeight.W700)
        }
        10.VerticalSpacer()
        CategoriesCompose(homeViewModel)
        Box(modifier = Modifier.height(180.dp)) {
            when (productsState) {
                is State.Loading -> {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 5.dp, vertical = 8.dp)
                            .fillMaxWidth()
                    ) {
                        LazyRow {
                            items(6) {
                                Box(modifier = Modifier.padding(horizontal = 8.dp)) {
                                    LoadingCard(150.dp, width = 130.dp)
                                }
                            }
                        }
                    }
                }
                
                is State.Succes -> {
                    val product = (productsState as State.Succes<Product>).data
                    val products = product.products
                    LazyRow(modifier = Modifier.padding(horizontal = 8.dp)) {
                        items(products) { product ->
                            Log.d("Testttt","Product id: ${product.id}")
                            ProductCategoryCardCompose(
                                product,
                                navHostController = navHostController
                            )
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