package com.example.foodrestaurantdeliveryapp.data.repository.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.foodrestaurantdeliveryapp.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreUserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {
    companion object {
        val THEME_KEY = stringPreferencesKey("app_theme")
        val LANGUAGE_KEY = stringPreferencesKey("language_code")
    }

    override val themeFlow: Flow<AppTheme> = dataStore.data
        .map { prefs ->
            prefs[THEME_KEY]?.let {
                key -> enumValues<AppTheme>().find { it.key == key } ?: AppTheme.SYSTEM
            } ?: AppTheme.SYSTEM
        }

    override suspend fun setTheme(theme: AppTheme) {
        dataStore.edit { prefs ->
            prefs[THEME_KEY] = theme.key
        }
    }

    override val languageFlow: Flow<String> = dataStore.data
        .map { it[LANGUAGE_KEY] ?: "en" }

    override suspend fun setLanguage(langCode: String) {
        dataStore.edit {
            it[LANGUAGE_KEY] = langCode
        }
    }
}
