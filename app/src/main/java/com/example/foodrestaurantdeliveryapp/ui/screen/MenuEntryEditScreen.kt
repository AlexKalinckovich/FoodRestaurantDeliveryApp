package com.example.foodrestaurantdeliveryapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.foodrestaurantdeliveryapp.R
import com.example.foodrestaurantdeliveryapp.localizedString
import com.example.foodrestaurantdeliveryapp.ui.view_model.MenuEntryEditUiState
import com.example.foodrestaurantdeliveryapp.ui.view_model.MenuEntryEditViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuEntryEditScreen(
    navigateBack: () -> Unit,
    viewModel: MenuEntryEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null && uiState.errorMessage == "Entry not found") {
            navigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (uiState.isAddMode) localizedString(R.string.add_menu_item)
                        else localizedString(R.string.edit_menu_item)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = localizedString(R.string.back))
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                MenuEntryForm(
                    uiState = uiState,
                    onNameChange = viewModel::updateName,
                    onDescriptionChange = viewModel::updateDescription,
                    onImageUrlChange = viewModel::updateImageUrl,
                    onPriceChange = viewModel::updatePrice,
                    onAvailableToggle = viewModel::toggleAvailable,
                    onCategorySelected = viewModel::selectCategory,
                    onSave = {
                        viewModel.save(
                            onSuccess = navigateBack,
                            onError = { error ->
                                // Здесь можно показать Snackbar, но для простоты игнорируем
                            }
                        )
                    },
                    onCancel = navigateBack,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuEntryForm(
    uiState: MenuEntryEditUiState,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onImageUrlChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onAvailableToggle: () -> Unit,
    onCategorySelected: (Int) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var expanded: Boolean by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .padding(16.dp)
            .imePadding()
            .verticalScroll(scrollState),  // Добавлено!
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = uiState.name,
            onValueChange = onNameChange,
            label = { Text(localizedString(R.string.name)) },
            isError = uiState.validationErrors.containsKey("name"),
            supportingText = {
                uiState.validationErrors["name"]?.let { Text(it) }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = uiState.description,
            onValueChange = onDescriptionChange,
            label = { Text(localizedString(R.string.description)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5
        )

        OutlinedTextField(
            value = uiState.imageUrl,
            onValueChange = onImageUrlChange,
            label = { Text(localizedString(R.string.image_url)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            val fillMaxWidth = Modifier
                .fillMaxWidth()
            OutlinedTextField(
                value = uiState.categories.find { it.categoryId == uiState.selectedCategoryId }?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text(localizedString(R.string.category)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = fillMaxWidth.menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = true
                ),
                isError = uiState.validationErrors.containsKey("category"),
                supportingText = {
                    uiState.validationErrors["category"]?.let { Text(it) }
                }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                uiState.categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            onCategorySelected(category.categoryId)
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = uiState.price,
            onValueChange = onPriceChange,
            label = { Text(localizedString(R.string.price)) },
            isError = uiState.validationErrors.containsKey("price"),
            supportingText = {
                uiState.validationErrors["price"]?.let { Text(it) }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = localizedString(R.string.available),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = uiState.isAvailable,
                onCheckedChange = { onAvailableToggle() }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                enabled = !uiState.isSaving
            ) {
                Text(localizedString(R.string.cancel))
            }
            Button(
                onClick = onSave,
                modifier = Modifier.weight(1f),
                enabled = !uiState.isSaving
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                } else {
                    Text(localizedString(R.string.save))
                }
            }
        }
    }
}