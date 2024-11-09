package com.example.trajanmarket.ui.navigation

import android.provider.ContactsContract
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.example.trajanmarket.ui.screens.login.LoginScreen
import kotlinx.serialization.Serializable

@Serializable
object Login

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    val navGraph = remember(navController) {
        navController.createGraph(
            startDestination = Login
        ) {
            composable<Login> {
                LoginScreen()
            }
        }
    }
    
    NavHost(
        navController = navController,
        graph = navGraph
    )
}
