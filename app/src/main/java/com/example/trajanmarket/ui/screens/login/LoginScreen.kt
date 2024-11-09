package com.example.trajanmarket.ui.screens.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LoginScreen() {
    Scaffold(
    ) { paddingValues ->
      Box(modifier=Modifier.padding(paddingValues)){
          Text("Hello Login")
      }
    }
}