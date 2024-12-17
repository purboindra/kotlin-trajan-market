package com.example.trajanmarket.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.trajanmarket.data.model.Product
import com.example.trajanmarket.ui.screens.cart.CartViewModel
import com.example.trajanmarket.ui.theme.gray1
import com.example.trajanmarket.utils.HorizontalSpacer
import com.example.trajanmarket.utils.VerticalSpacer

@Composable
fun CartItem(product: Product.Product, cartViewModel: CartViewModel) {
    val price = cartViewModel.getOriginalPrice(
        discountPercentage = product.discountPercentage,
        discountPrice = product.price
    )
    Surface(
        color = Color(0xffFFFFFF),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp).padding(vertical = 5.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)) {
            Box(
                modifier = Modifier.height(100.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                    ReusableAsyncImageWithLoading(
                        imageUrl = product.thumbnail,
                        modifier = Modifier
                            .width(100.dp)
                            .fillMaxHeight(),
                        contentDescription = product.title,
                    )
                    8.HorizontalSpacer()
                    Column {
                        Text(
                            product.title,
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = gray1,
                            ),
                        )
                        5.VerticalSpacer()
                        Row {
                            Text(
                                "$ ${product.price}",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.W600,
                                ),
                            )
                            5.HorizontalSpacer()
                            if (price != null) Text(
                                "$${price}",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    textDecoration = TextDecoration.LineThrough,
                                    fontWeight = FontWeight.W500,
                                    color = gray1,
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
    
}