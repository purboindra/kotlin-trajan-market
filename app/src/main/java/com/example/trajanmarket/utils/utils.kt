package com.example.trajanmarket.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.security.MessageDigest
import java.util.Base64
import java.security.SecureRandom
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

@RequiresApi(Build.VERSION_CODES.O)
fun generateSalt(): String {
    val random = SecureRandom()
    val salt = ByteArray(16)
    random.nextBytes(salt)
    return Base64.getEncoder().encodeToString(salt)
}

@RequiresApi(Build.VERSION_CODES.O)
fun hashPasswordSHA256(password: String, salt: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    val saltedPassword = password + salt
    val hash = md.digest(saltedPassword.toByteArray())
    return Base64.getEncoder().encodeToString(hash)
}