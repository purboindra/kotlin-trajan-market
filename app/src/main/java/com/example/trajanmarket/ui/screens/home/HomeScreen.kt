package com.example.trajanmarket.ui.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trajanmarket.utils.VerticalSpacer

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = hiltViewModel()) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            CategoryCompose(homeViewModel)
            20.VerticalSpacer()
            RecommendedCompose(homeViewModel)
        }
    }
}