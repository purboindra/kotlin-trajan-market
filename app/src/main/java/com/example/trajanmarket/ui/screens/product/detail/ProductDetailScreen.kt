package com.example.trajanmarket.ui.screens.product.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.ui.screens.product.ProductViewModel
import androidx.compose.material.icons.Icons as Icons1

@Composable
fun ProductDetailScreen(
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
            .padding(horizontal = 8.dp, vertical = 12.dp),
        topBar = {
            TopAppBar(
                title = {
                    Box() {}
                },
                backgroundColor = MaterialTheme.colorScheme.background,
                elevation = 0.dp,
                actions = {
                    Box(
                        modifier = Modifier
                            .height(48.dp)
                            .width(48.dp),
                    ) {
                        Icon(
                            Icons1.Outlined.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(38.dp)
                        )
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color.Black)
                                .align(Alignment.BottomEnd)
                        ) {
                            Text(
                                "1",
                                modifier = Modifier.align(Alignment.Center),
                                fontWeight = FontWeight.W500,
                                color = Color.White,
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                when (productByIdState) {
                    is State.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    
                    is State.Succes -> {
                        val product = (productByIdState as State.Succes).data
                        Text("Success")
                    }
                    
                    is State.Failure -> {
                        val throwable = (productByIdState as State.Failure).throwable
                        Text(throwable.message ?: "Unknown Error")
                    }
                    
                    else -> {}
                }
            }
        }
    }
}