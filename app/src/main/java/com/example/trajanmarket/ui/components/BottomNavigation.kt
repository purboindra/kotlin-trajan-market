package com.example.trajanmarket.ui.components

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavigationBar(
    items: List<Int>,
    selectedItem: Int,
    onItemSelected: (Int) -> Unit
) {
    BottomNavigation(backgroundColor = Color(0xffFFFFFF)) {
        items.forEachIndexed { index, item ->
            BottomNavigationItem(
                modifier = Modifier.padding(vertical = 12.dp),
                selected = selectedItem == index,
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Gray,
                icon = {
                    when (item) {
                        0 -> Icon(
                            Icons.Default.Home, contentDescription = "Home",
                            tint = if (selectedItem == index) Color(0xff5D3587) else Color.Gray
                        )
                        
                        1 -> Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = if (selectedItem == index) Color(0xff5D3587) else Color.Gray
                        )
                        
                        2 -> Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = "Cart",
                            tint = if (selectedItem == index) Color(0xff5D3587) else Color.Gray
                        )
                        
                        3 -> Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = if (selectedItem == index) Color(0xff5D3587) else Color.Gray
                        )
                    }
                },
                label = {
                    Text(
                        color = if (selectedItem == index) Color(0xff5D3587) else Color.Gray,
                        text = when (item) {
                            0 -> "Home"
                            1 -> "Cinemas"
                            2 -> "Ticket"
                            3 -> "Profile"
                            else -> "Unknown"
                        }
                    )
                },
                onClick = {
                    onItemSelected(index)
                }
            )
        }
    }
}