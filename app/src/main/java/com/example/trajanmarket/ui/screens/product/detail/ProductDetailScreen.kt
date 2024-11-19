package com.example.trajanmarket.ui.screens.product.detail

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trajanmarket.R
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.ui.components.ReusableAsyncImageWithLoading
import com.example.trajanmarket.ui.screens.product.ProductViewModel
import com.example.trajanmarket.ui.theme.gray1
import com.example.trajanmarket.ui.theme.grayLight
import com.example.trajanmarket.utils.HorizontalSpacer
import com.example.trajanmarket.utils.VerticalSpacer
import androidx.compose.material.icons.Icons as Icons1

@SuppressLint("ResourceType")
@Composable
fun ProductDetailScreen(
    productViewModel: ProductViewModel = hiltViewModel(),
    id: String
) {
    
    val productByIdState by productViewModel.productByIdState.collectAsState()
    val price by productViewModel.price.collectAsState()
    
    LaunchedEffect(Unit) {
        productViewModel.fetchProductById(id)
    }
    
    Scaffold(
        modifier = Modifier
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
                        Column {
                            10.VerticalSpacer()
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp),
                                shape = RoundedCornerShape(6.dp),
                                color = grayLight
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    ReusableAsyncImageWithLoading(
                                        imageUrl = product.thumbnail,
                                        modifier = Modifier
                                            .width(170.dp)
                                            .fillMaxHeight(),
                                        contentDescription = product.title,
                                    )
                                }
                            }
                            
                            10.VerticalSpacer()
                            
                            Column(modifier = Modifier.safeContentPadding()) {
                                Text(
                                    product.category,
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        color = gray1
                                    ),
                                )
                                5.VerticalSpacer()
                                Text(
                                    product.title,
                                    style = MaterialTheme.typography.titleMedium,
                                )
                                
                                10.VerticalSpacer()
                                
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.Red.copy(alpha = 0.1f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 8.dp
                                        )
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ticket_percent),
                                            contentDescription = "Disc",
                                            modifier = Modifier.size(32.dp),
                                            tint = Color.Red,
                                        )
                                        
                                        Text(
                                            "$ ${product.price}",
                                            style = MaterialTheme.typography.titleLarge.copy(
                                                color = Color.Red.copy(0.7f)
                                            ),
                                        )
                                        5.HorizontalSpacer()
                                        Text(
                                            "$${price}",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                textDecoration = TextDecoration.LineThrough,
                                                fontWeight = FontWeight.W500,
                                                color = Color.Red.copy(0.4f)
                                            ),
                                        )
                                    }
                                }
                            }
                        }
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