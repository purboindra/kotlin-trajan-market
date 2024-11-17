package com.example.trajanmarket.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trajanmarket.data.model.State
import com.example.trajanmarket.ui.components.LoadingShimmer

@Composable
fun CategoriesCompose(homeViewModel: HomeViewModel) {
    
    val categoriesState by homeViewModel.categories.collectAsState()
    val selectedCategory by homeViewModel.selectedCategory.collectAsState()
    
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        when (categoriesState) {
            is State.Loading -> {
                LoadingShimmer(height = 80.dp)
            }
            
            is State.Succes -> {
                val categories = (categoriesState as State.Succes<List<String>>).data
                LazyRow {
                    items(categories) { category ->
                        FilterChip(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            onClick = {
                                homeViewModel.onCategorySelect(category)
                            },
                            label = {
                                Text(category)
                            },
                            selected = selectedCategory == category,
                            leadingIcon = if (selectedCategory == category) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = "Done icon",
                                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                                    )
                                }
                            } else {
                                null
                            },
                        )
                    }
                }
            }
            
            is State.Failure -> {
                val throwable = (categoriesState as State.Failure).throwable
                Text(throwable.message ?: "Unknown Error")
            }
            
            else -> {}
        }
    }
    
    
}