package com.example.trajanmarket.ui.theme

import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = blackSecondary,
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
        color = blackSecondary,
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.W700,
        fontSize = 18.sp,
        color = blackSecondary,
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        color = blackSecondary,
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.W300,
        fontSize = 12.sp,
        fontFamily = FontFamily.Default,
        color = blackSecondary,
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        color = blackSecondary,
    ),
)