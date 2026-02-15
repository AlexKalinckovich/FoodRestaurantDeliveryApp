package com.example.foodrestaurantdeliveryapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.foodrestaurantdeliveryapp.R
import com.example.foodrestaurantdeliveryapp.data.entity.food.Category
import com.example.foodrestaurantdeliveryapp.data.entity.restaurant.Restaurant
import com.example.foodrestaurantdeliveryapp.localizedString
import com.example.foodrestaurantdeliveryapp.ui.view_model.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToDetail: (Int) -> Unit,
    navigateToSettings: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(localizedString(R.string.app_name)) },
                actions = {
                    IconButton(onClick = navigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = localizedString(R.string.settings))
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { homeViewModel.addSampleRestaurant() }) {
                Icon(Icons.Default.Add, contentDescription = localizedString(R.string.add))
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            CategoryChips(
                categories = uiState.categories,
                selectedCategoryId = uiState.selectedCategoryId,
                onCategorySelected = { homeViewModel.selectCategory(it) }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.restaurants) { restaurant ->
                    RestaurantCard(
                        restaurant = restaurant,
                        onItemClick = { navigateToDetail(restaurant.restaurantId) },
                        onDeleteClick = { homeViewModel.deleteRestaurant(restaurant) }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryChips(
    categories: List<Category>,
    selectedCategoryId: Int?,
    onCategorySelected: (Int?) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedCategoryId == null,
                onClick = { onCategorySelected(null) },
                label = { Text("Все") }
            )
        }
        items(categories) { category ->
            FilterChip(
                selected = selectedCategoryId == category.categoryId,
                onClick = { onCategorySelected(category.categoryId) },
                label = { Text(category.name) }
            )
        }
    }
}

@Composable
fun RestaurantCard(
    restaurant: Restaurant,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = restaurant.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(restaurant.name, style = MaterialTheme.typography.titleMedium)
                Text(restaurant.address, style = MaterialTheme.typography.bodySmall)
                Text("Доставка: ${restaurant.deliveryFee}", style = MaterialTheme.typography.labelSmall)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = localizedString(R.string.delete))
            }
        }
    }
}