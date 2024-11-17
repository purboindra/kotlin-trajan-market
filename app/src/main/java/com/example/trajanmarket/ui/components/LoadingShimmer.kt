package com.example.trajanmarket.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer

@Composable
fun LoadingShimmer(height: Dp? = null) {
    Row(
        modifier = Modifier
            .shimmer()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height ?: 24.dp)
                .background(Color.LightGray),
        )
    }
}

@Composable
fun LoadingCard(
    height: Dp? = null, width: Dp? = null, fillMaxSize: Boolean = false,
    fillMaxWidth: Boolean = false
) {
    Surface(
        shape = RoundedCornerShape(18)
    ) {
        Box(
            modifier = Modifier
                .shimmer()
                .then(
                    when {
                        fillMaxSize -> Modifier.fillMaxSize()
                        fillMaxWidth -> Modifier.fillMaxWidth()
                        else -> Modifier.width(width ?: 64.dp)
                    }
                )
                .width(width ?: 64.dp)
                .height(height ?: 24.dp)
                .background(Color.LightGray),
        )
    }
}