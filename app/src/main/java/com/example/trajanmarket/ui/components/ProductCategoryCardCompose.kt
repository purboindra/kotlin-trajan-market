package com.example.trajanmarket.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.trajanmarket.data.model.Product
import com.example.trajanmarket.ui.theme.grayLight
import com.example.trajanmarket.utils.VerticalSpacer

@Composable
fun ProductCategoryCardCompose( product: Product.Product){
    Surface(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 6.dp),
        color = grayLight,
        shape = RoundedCornerShape(18)
    ) {
        Box(
            modifier = Modifier
                .width(166.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(product.thumbnail).crossfade(true)
                        .build(),
                    contentDescription = product.title,
                    modifier = Modifier.height(100.dp),
                    contentScale = ContentScale.Fit,
                )
                
                8.VerticalSpacer()
                
                Text(
                    product.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W700,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
                
                8.VerticalSpacer()
                
            }
        }
    }
}