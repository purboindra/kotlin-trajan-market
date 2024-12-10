package com.example.trajanmarket.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.trajanmarket.R
import com.example.trajanmarket.data.model.Product
import com.example.trajanmarket.utils.HorizontalSpacer

@Composable
fun PriceContainerCompose(product: Product.Product, price: Double) {
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
                modifier = Modifier.size(24.dp),
                tint = Color.Red,
            )
            
            5.HorizontalSpacer()
            
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