package com.example.trajanmarket.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = hiltViewModel()) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            item {
                CategoryCompose()
            }
        }
    }
}