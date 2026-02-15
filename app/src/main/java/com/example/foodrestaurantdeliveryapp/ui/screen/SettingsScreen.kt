package com.example.foodrestaurantdeliveryapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.foodrestaurantdeliveryapp.R
import com.example.foodrestaurantdeliveryapp.localizedString
import com.example.foodrestaurantdeliveryapp.ui.theme.AppTheme
import com.example.foodrestaurantdeliveryapp.ui.view_model.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val appTheme by viewModel.appTheme.collectAsState()
    val languageCode by viewModel.languageCode.collectAsState()
    LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(localizedString(R.string.settings))},
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            
            Text(
                text = localizedString(R.string.theme),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            
            AppTheme.entries.forEach { theme ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (appTheme == theme),
                            onClick = { viewModel.setTheme(theme) }
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (appTheme == theme),
                        onClick = { viewModel.setTheme(theme) }
                    )
                    Text(
                        text = theme.displayName,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(height = 24.dp))

            Text(
                text = localizedString(R.string.language),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(height = 8.dp))
            
            Button(
                onClick = { viewModel.setLanguage("en") },
                enabled = languageCode != "en"
            ) {
                Text(localizedString(R.string.english))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = { viewModel.setLanguage("ru") },
                enabled = languageCode != "ru"
            ) {
                Text(localizedString(R.string.russian))
            }
        }
    }
}


private val AppTheme.displayName: String
    @Composable
    get() = when (this) {
        AppTheme.LIGHT  -> localizedString(R.string.light)
        AppTheme.DARK   -> localizedString(R.string.dark)
        AppTheme.SYSTEM -> localizedString(R.string.system_default)
    }