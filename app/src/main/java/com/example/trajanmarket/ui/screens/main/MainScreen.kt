package com.example.trajanmarket.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trajanmarket.ui.components.BottomNavigationBar

@Composable
fun MainScreen(mainViewModel: MainViewModel = hiltViewModel()) {
    
    val bottomNavbarIndex by mainViewModel.bottomNavbarIndex.collectAsState()
    
    val bodies = listOf<@Composable () -> Unit>(
        {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = "Placeholder for Home")
            }
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
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = "Placeholder for Ticket")
            }
        },
        {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = "Placeholder for Profile")
            }
        }
    )
    
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
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            bodies[bottomNavbarIndex]()
        }
    }
}