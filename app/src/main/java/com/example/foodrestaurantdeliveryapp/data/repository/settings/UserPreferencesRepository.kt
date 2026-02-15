package com.example.foodrestaurantdeliveryapp.data.repository.settings

import com.example.foodrestaurantdeliveryapp.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow


interface UserPreferencesRepository {
    val themeFlow: Flow<AppTheme>
    val languageFlow: Flow<String>
    suspend fun setTheme(theme: AppTheme)
    suspend fun setLanguage(langCode: String)
}