package com.example.trajanmarket.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun CategoryCompose() {
    Box(modifier = Modifier.fillMaxWidth()) {
        Box(contentAlignment = Alignment.Center) {
            Text("Browse by Category", fontSize = 18.sp, fontWeight = FontWeight.W700)
        }
    }
}