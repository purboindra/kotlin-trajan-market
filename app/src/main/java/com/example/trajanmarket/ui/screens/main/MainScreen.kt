package com.example.trajanmarket.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.trajanmarket.ui.components.BottomNavigationBar
import com.example.trajanmarket.ui.screens.cart.CartScreen
import com.example.trajanmarket.ui.screens.home.HomeScreen
import com.example.trajanmarket.ui.screens.profile.ProfileScreen

@Composable
fun MainScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    navHostController: NavHostController,
    bottomNavbar: Int?
) {
    
    val bottomNavbarIndex by mainViewModel.bottomNavbarIndex.collectAsState()
    
    val bodies = listOf<@Composable () -> Unit>(
        {
            HomeScreen(navHostController = navHostController)
        },
        {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = "Placeholder for Cinemas")
            }
        },
        {
            CartScreen(navHostController = navHostController)
        },
        {
            ProfileScreen(navHostController = navHostController)
        }
    )
    
    LaunchedEffect(bottomNavbar) {
        bottomNavbar?.let {
            if (it != -1) {
                mainViewModel.onChangeBottomNavbarIndex(it)
            }
        }
    }
    
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = bodies.indices.toList(),
                selectedItem = bottomNavbarIndex,
                onItemSelected = { index ->
                    mainViewModel.onChangeBottomNavbarIndex(index)
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .padding(vertical = 12.dp)
                .fillMaxSize()
        ) {
            bodies[bottomNavbarIndex]()
        }
    }
}