package com.example.foodrestaurantdeliveryapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.foodrestaurantdeliveryapp.data.entity.restaurant.Restaurant
import com.example.foodrestaurantdeliveryapp.ui.view_model.HomeViewModel

import com.example.foodrestaurantdeliveryapp.R
import com.example.foodrestaurantdeliveryapp.localizedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToDetail: (Int) -> Unit,
    navigateToSettings: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
)  {
    val uiState by homeViewModel.homeUiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    localizedString(R.string.app_name)) },
                actions = {
                    IconButton(onClick = navigateToSettings) {
                        Icon(imageVector = Icons.Default.Settings,
                             contentDescription = localizedString(R.string.settings))
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { homeViewModel.addSampleRestaurant() }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = localizedString(R.string.add))
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues = innerPadding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = uiState.restaurantList) { restaurant ->
                RestaurantItem(
                    restaurant = restaurant,
                    onItemClick = { navigateToDetail(restaurant.restaurantId) },
                    onDeleteClick = { homeViewModel.deleteRestaurant(restaurant) }
                )
            }
        }
    }
}

@Composable
fun RestaurantItem(
    restaurant: Restaurant,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onItemClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(all = 16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = restaurant.name, style = MaterialTheme.typography.titleMedium)
                Text(text = restaurant.address, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete,
                     contentDescription = localizedString(R.string.delete))
            }
        }
    }
}