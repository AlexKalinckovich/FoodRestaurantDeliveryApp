package com.example.foodrestaurantdeliveryapp


import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

val LocalLocalizedContext = compositionLocalOf<Context> {
    error("No localized context provided")
}

@Composable
fun localizedString(@StringRes id: Int): String {
    return LocalLocalizedContext.current.
    resources.getString(id)
}

@Composable
fun localizedString(@StringRes id: Int, vararg formatArgs: Any): String {
    return LocalLocalizedContext.current.
    resources.getString(id, *formatArgs)
}