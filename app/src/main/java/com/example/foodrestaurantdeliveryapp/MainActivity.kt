package com.example.foodrestaurantdeliveryapp

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.foodrestaurantdeliveryapp.data.DatabaseInitializer
import com.example.foodrestaurantdeliveryapp.data.repository.auth.AuthRepository
import com.example.foodrestaurantdeliveryapp.data.repository.model.restaurant.RestaurantRepository
import com.example.foodrestaurantdeliveryapp.service.NotificationHelper
import com.example.foodrestaurantdeliveryapp.ui.FoodNavHost
import com.example.foodrestaurantdeliveryapp.ui.screen.AuthScreen
import com.example.foodrestaurantdeliveryapp.ui.theme.AppTheme
import com.example.foodrestaurantdeliveryapp.ui.theme.FoodRestaurantDeliveryAppTheme
import com.example.foodrestaurantdeliveryapp.ui.view_model.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    @Inject lateinit var databaseInitializer: DatabaseInitializer
    @Inject lateinit var restaurantRepository: RestaurantRepository
    @Inject lateinit var authRepository: AuthRepository

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        databaseInitializer.initialize()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
                NotificationHelper.startService(this)
            } else {
                NotificationHelper.startService(this)
            }
        } else {
            NotificationHelper.startService(this)
        }

        setContent {
            val langCode: String by settingsViewModel.languageCode.collectAsStateWithLifecycle()
            val localizedContext = remember(key1 = langCode) {
                createLocalizedContext(context = this, langCode)
            }

            CompositionLocalProvider(value = LocalLocalizedContext.provides(value = localizedContext)) {
                val appTheme: AppTheme by settingsViewModel.appTheme.collectAsStateWithLifecycle()
                FoodRestaurantDeliveryAppTheme(appTheme = appTheme) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val currentUser by authRepository.currentUser.collectAsStateWithLifecycle(initialValue = null)

                        if (currentUser == null && authRepository.isSignedIn()) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        } else if (currentUser != null) {
                            val navController = rememberNavController()
                            FoodNavHost(navController = navController)
                        } else {
                            AuthScreen(onAuthSuccess = {})
                        }
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
        val locale: Locale = Locale.forLanguageTag(langCode)
        val config = Configuration(context.resources.configuration).also {
            it.setLocale(locale)
        }
        return context.createConfigurationContext(config)
    }
}