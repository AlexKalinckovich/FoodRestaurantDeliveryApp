package com.example.foodrestaurantdeliveryapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.foodrestaurantdeliveryapp.R
import com.example.foodrestaurantdeliveryapp.data.entity.food.FoodProduct
import com.example.foodrestaurantdeliveryapp.localizedString
import com.example.foodrestaurantdeliveryapp.ui.view_model.FoodProductsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodProductSearchScreen(
    navigateBack: () -> Unit,
    viewModel: FoodProductsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isConnected by viewModel.networkMonitor.isConnected.collectAsStateWithLifecycle()
    var barcodeInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(localizedString(R.string.search_layout)) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isConnected) Icons.Default.Wifi else Icons.Default.WifiOff,
                    contentDescription = null,
                    tint = if (isConnected) Color.Green else Color.Red
                )
                Text(
                    text = if (isConnected) localizedString(R.string.online_mode) else localizedString(R.string.offline_mode),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = barcodeInput,
                onValueChange = { barcodeInput = it },
                label = { Text("${localizedString(R.string.barcode)} (e.g., 3017620422003)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.searchProduct(barcodeInput) },
                modifier = Modifier.fillMaxWidth(),
                enabled = barcodeInput.isNotBlank()
            ) {
                Text(localizedString(R.string.search))
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                uiState.isLoading -> CircularProgressIndicator()
                uiState.error != null -> Text("${localizedString(R.string.error)}: ${uiState.error}", color = MaterialTheme.colorScheme.error)
                uiState.product != null -> {
                    if (uiState.isOffline) {
                        Text(
                            text = localizedString(R.string.offline_mode),
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    ProductDetails(uiState.product!!)
                }
            }
        }
    }
}


@Composable
fun ProductDetails(product: FoodProduct) {
    val showFullImage = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = product.name,
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "${localizedString(R.string.barcode)}: ${product.barcode}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

            product.brand?.let {
                Text(
                    text = "${localizedString(R.string.brand)}: $it",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            product.calories?.let {
                Text(
                    text = "${localizedString(R.string.calories)}: $it kcal",
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            product.ingredients?.let {
                Text(
                    text = "${localizedString(R.string.ingredients)}: $it",
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            product.imageUrl?.let { url ->
                Spacer(modifier = Modifier.height(height = 8.dp))

                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 200.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .clickable { showFullImage.value = true }
                ) {
                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop
                    )
                    
                    Text(
                        text = localizedString(R.string.tap_image_action),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(8.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        softWrap = false
                    )
                }
            }
        }
    }

    
    if (showFullImage.value && product.imageUrl != null) {
        Dialog(onDismissRequest = { showFullImage.value = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 400.dp)
                    .padding(all = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { showFullImage.value = false } 
                ) {
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit 
                    )
                    
                    Button(
                        onClick = { showFullImage.value = false },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(all = 8.dp)
                    ) {
                        Text(text = localizedString(R.string.close))
                    }
                }
            }
        }
    }
}