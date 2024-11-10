package com.example.trajanmarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.trajanmarket.ui.navigation.AppNavHost
import com.example.trajanmarket.ui.screens.login.LoginViewModel
import com.example.trajanmarket.ui.theme.TrajanMarketTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        
        val loginViewModelL: LoginViewModel by viewModels()
        
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrajanMarketTheme {
                MainAppContent()
            }
        }
    }
}

@Composable
fun MainAppContent() {
    val navController = rememberNavController()
    AppNavHost(navController = navController)
}