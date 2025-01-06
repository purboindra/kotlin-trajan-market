package com.example.trajanmarket.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.navArgument
import com.example.trajanmarket.ui.screens.login.LoginScreen
import com.example.trajanmarket.ui.screens.main.MainScreen
import com.example.trajanmarket.ui.screens.maps.OsmdroidMapScreen
import com.example.trajanmarket.ui.screens.product.detail.ProductDetailScreen
import com.example.trajanmarket.ui.screens.profile.ProfileScreen
import com.example.trajanmarket.ui.screens.register.RegisterScreen
import com.example.trajanmarket.ui.screens.splash.SplashScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    val navGraph = remember(navController) {
        navController.createGraph(
            startDestination = Splash
        ) {
            composable<Splash> {
                SplashScreen(navHostController = navController)
            }
            composable<Login> {
                LoginScreen(navHostController = navController)
            }
            composable<Register> {
                RegisterScreen(navHostController = navController)
            }
            composable(route = "main?bottomNavbarIndex={bottomNavbarIndex}",
                arguments = listOf(
                    navArgument("bottomNavbarIndex") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val bottomNavbar = backStackEntry.arguments?.getString("bottomNavbarIndex") ?: "-1"
                MainScreen(navHostController = navController, bottomNavbar = bottomNavbar.toInt())
            }
            composable<Profile> {
                ProfileScreen(navHostController = navController)
            }
            composable<OsmdroidMaps> {
                OsmdroidMapScreen(navHostController = navController)
            }
            composable(
                route = "product_detail/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")
                productId?.let {
                    ProductDetailScreen(id = it, navHostController = navController)
                }
            }
        }
    }
    NavHost(
        navController = navController,
        graph = navGraph
    )
}
