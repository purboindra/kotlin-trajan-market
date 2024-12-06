package com.example.trajanmarket.ui.screens.profile

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.trajanmarket.ui.theme.grayLight
import com.example.trajanmarket.utils.VerticalSpacer
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    navHostController: NavHostController,
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    
    val logoutLoadingState = profileViewModel.loadingLogout.collectAsState()
    
    val coroutineScope = rememberCoroutineScope()
    
    Box(
        modifier = Modifier
            .safeContentPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Hello")
            10.VerticalSpacer()
            ElevatedButton(
                onClick = {
                    coroutineScope.launch {
                        profileViewModel.logout(navHostController)
                    }
                }
            ) {
                if (logoutLoadingState.value) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(26.dp),
                        color = grayLight,
                        strokeWidth = 5.dp
                    )
                } else {
                    Text("Log Out")
                }
            }
        }
    }
}