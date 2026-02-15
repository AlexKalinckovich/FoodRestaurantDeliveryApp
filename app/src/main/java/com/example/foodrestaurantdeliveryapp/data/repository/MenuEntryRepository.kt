package com.example.foodrestaurantdeliveryapp.data.repository

import com.example.foodrestaurantdeliveryapp.data.dao.menu.MenuEntryDao
import com.example.foodrestaurantdeliveryapp.data.entity.menu.MenuEntry
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class MenuEntryRepository @Inject constructor(
    private val menuEntryDao: MenuEntryDao
) {
    suspend fun insertMenuEntries(vararg menuEntries: MenuEntry) {
        menuEntryDao.insertAll(*menuEntries)
    }
}