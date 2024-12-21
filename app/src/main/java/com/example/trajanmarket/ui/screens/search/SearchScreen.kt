package com.example.trajanmarket.ui.screens.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.trajanmarket.data.model.Product
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.ui.components.CartItem
import com.example.trajanmarket.ui.theme.grayLight
import com.example.trajanmarket.utils.VerticalSpacer
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    navHostController: NavHostController,
    searchViewModel: SearchViewModel = hiltViewModel()
) {
    
    val query by searchViewModel.searchQuery.collectAsState()
    val searchState by searchViewModel.searchState.collectAsState()
    
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    val coroutineScope = rememberCoroutineScope()
    
    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                searchViewModel.observeSearchQuery()
            }
        }
        
        lifecycle.addObserver(observer)
        
        onDispose {
            searchViewModel.onQueryChanged("")
            lifecycle.removeObserver(observer)
        }
        
    }
    
    Column(modifier = Modifier.safeContentPadding()) {
        OutlinedTextField(
            value = query,
            onValueChange = { query ->
                searchViewModel.onQueryChanged(query)
            },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text("Search product...")
            },
            singleLine = true,
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            searchViewModel.onQueryChanged("")
                            searchViewModel.resetState()
                        }
                    }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Clear Text")
                    }
                }
            },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Blue,
                unfocusedTextColor = grayLight,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
            )
        )
        
        20.VerticalSpacer()
        
        when (searchState) {
            is State.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            
            is State.Succes -> {
                val product = (searchState as State.Succes<Product>).data
                LazyColumn {
                    items(product.products) { item ->
                        CartItem(item, onClick = {
//                            handleClickItem(item.id)
                        })
                    }
                }
            }
            
            is State.Failure -> {
                val throwable = (searchState as State.Failure).throwable
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(throwable.message ?: "Unknown Error")
                }
            }
            
            else -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Search your favorite product...")
                }
            }
        }
        
    }
}