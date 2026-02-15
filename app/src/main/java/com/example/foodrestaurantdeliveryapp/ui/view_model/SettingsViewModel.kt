package com.example.foodrestaurantdeliveryapp.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrestaurantdeliveryapp.data.repository.settings.UserPreferencesRepository
import com.example.foodrestaurantdeliveryapp.ui.theme.AppTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepo: UserPreferencesRepository
) : ViewModel() {

    private val _appTheme = MutableStateFlow<AppTheme>(value = AppTheme.SYSTEM)
    val appTheme: StateFlow<AppTheme> = _appTheme.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferencesRepo.themeFlow
                .distinctUntilChanged()
                .collect { theme ->
                    _appTheme.value = theme
                }
        }
    }

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            userPreferencesRepo.setTheme(theme)
        }
    }

    private val _languageCode = MutableStateFlow(value = "en")
    val languageCode: StateFlow<String> = _languageCode.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferencesRepo.languageFlow
                .distinctUntilChanged()
                .collect { lang ->
                    _languageCode.value = lang
                }
        }
    }

    fun setLanguage(langCode: String) {
        viewModelScope.launch {
            userPreferencesRepo.setLanguage(langCode)
        }
    }
}

