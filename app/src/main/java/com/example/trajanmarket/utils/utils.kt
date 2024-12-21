package com.example.trajanmarket.utils

import android.annotation.SuppressLint
import kotlin.math.roundToInt

@SuppressLint("DefaultLocale")
fun getOriginalPrice(discountPercentage: Double, discountPrice: Double): Int? {
    if (discountPercentage >= 100.0 || discountPercentage < 0) {
        return null
    }
    val realPrice = discountPrice / (1 - (discountPercentage / 100))
    val formattedNumber = String.format("%.2f", realPrice).toDouble()
    return formattedNumber.roundToInt()
}