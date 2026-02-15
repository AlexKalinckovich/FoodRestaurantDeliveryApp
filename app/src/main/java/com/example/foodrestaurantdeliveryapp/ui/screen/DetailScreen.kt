package com.example.foodrestaurantdeliveryapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.foodrestaurantdeliveryapp.ui.view_model.DetailViewModel

import com.example.foodrestaurantdeliveryapp.R
import com.example.foodrestaurantdeliveryapp.localizedString


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navigateBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
)  {
    val restaurant by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = restaurant?.name ?: localizedString(R.string.unknown)) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = localizedString(R.string.back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(paddingValues = innerPadding).padding(all = 16.dp)) {
            Text(text = "${localizedString(R.string.address)}: ${restaurant?.address}", style = MaterialTheme.typography.bodyLarge)
            Text(text = "${localizedString(R.string.delivery_fee)}: $${restaurant?.deliveryFee}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}