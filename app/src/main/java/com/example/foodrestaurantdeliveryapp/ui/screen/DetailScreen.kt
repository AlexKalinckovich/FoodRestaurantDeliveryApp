package com.example.foodrestaurantdeliveryapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.foodrestaurantdeliveryapp.data.dao.menu.MenuWithDetails
import com.example.foodrestaurantdeliveryapp.data.entity.restaurant.Restaurant
import com.example.foodrestaurantdeliveryapp.localizedString
import com.example.foodrestaurantdeliveryapp.ui.view_model.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navigateBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.restaurant?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = localizedString(R.string.back))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(paddingValues = innerPadding)) {
            uiState.restaurant?.let { restaurant ->
                RestaurantHeader(restaurant)
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(all = 16.dp),
                verticalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                items(uiState.menuItems) { menuItem ->
                    MenuItemCard(menuItem)
                }
            }
        }
    }
}

@Composable
fun RestaurantHeader(restaurant: Restaurant) {
    Column(modifier = Modifier.padding(16.dp)) {
        AsyncImage(
            model = restaurant.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 125.dp)
                .clip(shape = RoundedCornerShape(size = 12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = restaurant.name, style = MaterialTheme.typography.headlineSmall)
        Text(text = restaurant.address, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = "${localizedString(R.string.delivery_fee)}: ${restaurant.deliveryFee}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun MenuItemCard(item: MenuWithDetails) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(size = 60.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(width = 12.dp))
            Column(modifier = Modifier.weight(weight = 1f)) {
                Text(text = item.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )
                Text(
                    text = item.price,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}