package com.example.foodrestaurantdeliveryapp.data.repository.model.menu

import com.example.foodrestaurantdeliveryapp.data.dao.menu.MenuEntryDao
import com.example.foodrestaurantdeliveryapp.data.entity.menu.MenuEntry
import com.example.foodrestaurantdeliveryapp.data.repository.model.menu.model.MenuEntryWithFood
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class MenuEntryRepository @Inject constructor(
    private val menuEntryDao: MenuEntryDao
) {
    suspend fun insertMenuEntries(vararg menuEntries: MenuEntry) {
        menuEntryDao.insertAll(*menuEntries)
    }

    suspend fun getMenuEntryById(menuId: Int): MenuEntry? = menuEntryDao.getMenuEntry(menuId)

    suspend fun getMenuEntryWithFood(menuId: Int): MenuEntryWithFood? =
        menuEntryDao.getMenuEntryWithFood(menuId)

    suspend fun updateMenuEntry(menuEntry: MenuEntry) = menuEntryDao.updateMenuEntry(menuEntry)

    suspend fun insertMenuEntry(menuEntry: MenuEntry): Long = menuEntryDao.insertMenuEntry(menuEntry)

    suspend fun deleteAll() {
        menuEntryDao.deleteAll()
    }
}