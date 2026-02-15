package com.example.foodrestaurantdeliveryapp

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.foodrestaurantdeliveryapp.data.DatabaseInitializer
import com.example.foodrestaurantdeliveryapp.ui.FoodNavHost
import com.example.foodrestaurantdeliveryapp.ui.theme.FoodRestaurantDeliveryAppTheme
import com.example.foodrestaurantdeliveryapp.ui.view_model.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    @Inject
    lateinit var databaseInitializer: DatabaseInitializer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        databaseInitializer.initialize()
        setContent {
            val langCode by settingsViewModel.languageCode.collectAsState()
            val localizedContext = remember(langCode) {
                createLocalizedContext(this, langCode)
            }

            CompositionLocalProvider(LocalLocalizedContext provides localizedContext) {
                val appTheme by settingsViewModel.appTheme.collectAsState()
                FoodRestaurantDeliveryAppTheme(appTheme = appTheme) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()

                        FoodNavHost(navController = navController)
                    }
                }
            }
        }

        lifecycleScope.launch {
            settingsViewModel.languageCode.collect { langCode ->
                AppCompatDelegate.setApplicationLocales(
                    LocaleListCompat.forLanguageTags(langCode)
                )
            }
        }
    }

    private fun createLocalizedContext(context: Context, langCode: String): Context {
        val locale = Locale.forLanguageTag(langCode)

        val config = Configuration(context.resources.configuration)

        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }
}