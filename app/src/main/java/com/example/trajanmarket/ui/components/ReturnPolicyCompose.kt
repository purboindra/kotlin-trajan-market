package com.example.trajanmarket.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.trajanmarket.utils.HorizontalSpacer

@Composable
fun ReturnPolicyCompose(policyText: String, modifier: Modifier) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color.Red.copy(alpha = 0.1f),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = modifier.padding(
                vertical = 8.dp,
                horizontal = 5.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Return Policy: ",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = Color.Red.copy(0.7f),
                    fontSize = 12.sp
                ),
            )
            3.HorizontalSpacer()
            Text(
                policyText,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = Color.Red.copy(0.7f),
                    fontSize = 12.sp
                ),
            )
        }
    }
}