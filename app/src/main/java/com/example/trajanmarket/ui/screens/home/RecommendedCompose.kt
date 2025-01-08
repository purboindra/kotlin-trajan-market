package com.example.trajanmarket.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavHostController
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.ui.components.LoadingCard
import com.example.trajanmarket.ui.components.ProductCategoryCardCompose
import com.example.trajanmarket.utils.VerticalSpacer

@Composable
fun RecommendedCompose(homeViewModel: HomeViewModel, navHostController: NavHostController) {
    
    val productState by homeViewModel.products.collectAsState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {
        Column {
            Text(
                "Recommended For You",
                fontSize = 18.sp, fontWeight = FontWeight.W700
            )
            10.VerticalSpacer()
            when (productState) {
                is State.Loading -> {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 5.dp, vertical = 8.dp)
                            .fillMaxSize()
                    ) {
                        LazyVerticalGrid(
                            modifier = Modifier.height(480.dp),
                            columns = GridCells.Adaptive(minSize = 128.dp),
                            userScrollEnabled = false
                        ) {
                            items(6) {
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 12.dp)
                                ) {
                                    LoadingCard(150.dp, fillMaxWidth = true)
                                }
                            }
                        }
                    }
                }
                
                is State.Succes -> {
                    val product = (productState as State.Succes).data
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(((product.size - 1) * 120.dp))
                    ) {
                        LazyVerticalGrid(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            columns = GridCells.Fixed(2),
                            
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
                            userScrollEnabled = false,
                        ) {
                            items(product) { product ->
                                ProductCategoryCardCompose(
                                    product,
                                    showPrice = true,
                                    navHostController = navHostController
                                )
                            }
                        }
                    }
                }
                
                is State.Failure -> {
                    val throwable = (productState as State.Failure)
                }
                
                else -> {}
            }
        }
    }
}