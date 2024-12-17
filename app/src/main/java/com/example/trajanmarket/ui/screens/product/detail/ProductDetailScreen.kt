package com.example.trajanmarket.ui.screens.product.detail

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.data.remote.api.AddToCartParams
import com.example.trajanmarket.ui.components.PriceContainerCompose
import com.example.trajanmarket.ui.components.ReturnPolicyCompose
import com.example.trajanmarket.ui.components.ReusableAsyncImageWithLoading
import com.example.trajanmarket.ui.screens.cart.CartViewModel
import com.example.trajanmarket.ui.screens.product.ProductViewModel
import com.example.trajanmarket.ui.theme.blue100
import com.example.trajanmarket.ui.theme.gray1
import com.example.trajanmarket.ui.theme.grayLight
import com.example.trajanmarket.ui.theme.green
import com.example.trajanmarket.utils.HorizontalSpacer
import com.example.trajanmarket.utils.VerticalSpacer
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons as Icons1

@SuppressLint("ResourceType")
@Composable
fun ProductDetailScreen(
    productViewModel: ProductViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    id: String,
) {
    
    val productByIdState by productViewModel.productByIdState.collectAsState()
    val price by productViewModel.price.collectAsState()
    val hasProductInCart by productViewModel.hasProductInCart.collectAsState()
    val addToCartState by cartViewModel.addToCartState.collectAsState()
    val cartListState by cartViewModel.cartListState.collectAsState()
    val removeFromCartState by cartViewModel.removeFromCartState.collectAsState()
    
    val coroutineScope = rememberCoroutineScope()
    
    val snackbarHostState = remember { SnackbarHostState() }
    
    var snackbarColor by remember {
        mutableStateOf(Color.Red)
    }
    
    LaunchedEffect(Unit) {
        productViewModel.fetchProductById(id)
        cartViewModel.getCarts()
        productViewModel.checkProductInCart(id)
    }
    
    LaunchedEffect(addToCartState) {
        when (addToCartState) {
            is State.Failure -> {
                val throwable = (addToCartState as State.Failure).throwable
                snackbarHostState.showSnackbar(throwable.message ?: "Unknown Error Occurred")
            }
            
            is State.Succes -> {
                cartViewModel.getCarts()
                productViewModel.checkProductInCart(id)
                snackbarColor = green
                snackbarHostState.showSnackbar("Success add to cart!")
            }
            
            else -> {}
        }
    }
    
    LaunchedEffect(removeFromCartState) {
        when (removeFromCartState) {
            is State.Failure -> {
                val throwable = (removeFromCartState as State.Failure).throwable
                snackbarColor = Color.Red
                snackbarHostState.showSnackbar(throwable.message ?: "Unknown Error Occurred")
            }
            
            is State.Succes -> {
                cartViewModel.getCarts()
                productViewModel.checkProductInCart(id)
                snackbarColor = green
                snackbarHostState.showSnackbar("Success remove from cart!")
            }
            
            else -> {}
        }
    }
    
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(containerColor = snackbarColor, snackbarData = data)
            }
        },
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
                            if (cartListState is State.Loading) Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = grayLight,
                                    strokeWidth = 3.dp
                                )
                            } else Text(
                                "${(cartListState as? State.Succes)?.data?.size}",
                                modifier = Modifier.align(Alignment.Center),
                                fontWeight = FontWeight.W500,
                                color = Color.White,
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            ElevatedButton(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(
                    8.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = blue100,
                    disabledContainerColor = gray1,
                ),
                enabled = addToCartState !is State.Loading &&
                        removeFromCartState !is State.Loading &&
                        cartListState !is State.Loading,
                onClick = {
                    coroutineScope.launch {
                        if (!hasProductInCart) {
                            cartViewModel.addToCart(
                                listOf(
                                    AddToCartParams(
                                        id = id,
                                        quantity = 1
                                    )
                                )
                            )
                        } else {
                            cartViewModel.removeFromCart(id)
                        }
                    }
                }
            ) {
                if (addToCartState is State.Loading || removeFromCartState is State.Loading || cartListState is State.Loading) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(26.dp),
                            color = grayLight,
                            strokeWidth = 5.dp
                        )
                    }
                } else {
                    Text(
                        if (hasProductInCart) "Remove" else "+ Keranjang",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = Color.White,
                        )
                    )
                }
                
                
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                when (productByIdState) {
                    is State.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .height(400.dp),
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
                                
                                5.VerticalSpacer()
                                
                                LazyRow() {
                                    items(product.images) { item ->
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            ReusableAsyncImageWithLoading(
                                                imageUrl = item,
                                                modifier = Modifier
                                                    .width(64.dp)
                                                    .height(64.dp),
                                                contentDescription = "Image Preview",
                                            )
                                        }
                                    }
                                }
                                
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
                                
                                PriceContainerCompose(product, price)
                                8.VerticalSpacer()
                                HorizontalDivider(
                                    thickness = 1.dp, color = gray1.copy(
                                        0.5f
                                    )
                                )
                                8.VerticalSpacer()
                                Text(
                                    product.description,
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        color = Color.Black.copy(0.8f),
                                    ),
                                )
                                13.VerticalSpacer()
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Tersisa", style = MaterialTheme.typography.labelLarge)
                                    5.HorizontalSpacer()
                                    Text(
                                        product.stock.toString(),
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                    8.HorizontalSpacer()
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        border = BorderStroke(1.dp, gray1),
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(
                                                vertical = 5.dp,
                                                horizontal = 9.dp
                                            ),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Icon(
                                                Icons.Default.Star, contentDescription = null,
                                                tint = Color.Yellow.copy(
                                                    0.8f
                                                ),
                                                modifier = Modifier.size(18.dp),
                                            )
                                            3.HorizontalSpacer()
                                            Text(
                                                "(${product.rating})",
                                                style = MaterialTheme.typography.labelLarge.copy(
                                                    color = gray1.copy(
                                                        0.8f,
                                                    )
                                                )
                                            )
                                        }
                                    }
                                }
                                15.VerticalSpacer()
                                ReturnPolicyCompose(
                                    policyText = product.returnPolicy, modifier = Modifier
                                )
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