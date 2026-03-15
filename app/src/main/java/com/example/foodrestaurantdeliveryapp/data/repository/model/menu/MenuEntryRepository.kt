package com.example.foodrestaurantdeliveryapp.data.repository.model.menu

import com.example.foodrestaurantdeliveryapp.data.dao.menu.MenuEntryDao
import com.example.foodrestaurantdeliveryapp.data.entity.menu.MenuEntry
import com.example.foodrestaurantdeliveryapp.data.repository.model.menu.model.MenuEntryWithFood
import com.example.foodrestaurantdeliveryapp.data.repository.model.menu.model.MenuWithDetails
import com.example.foodrestaurantdeliveryapp.utils.SearchTokenGenerator
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class MenuEntryRepository @Inject constructor(
    private val menuEntryDao: MenuEntryDao
) {
    fun getMenuForRestaurant(restaurantId: String): Flow<List<MenuWithDetails>> =
        menuEntryDao.getMenuForRestaurant(restaurantId)

    suspend fun insertMenuEntries(vararg menuEntries: MenuEntry) {
        menuEntryDao.insertAll(*menuEntries)
    }

    suspend fun getMenuEntryById(menuId: String): MenuEntry? =
        menuEntryDao.getMenuEntry(menuId)

    suspend fun getMenuEntryWithFood(menuId: String): MenuEntryWithFood? =
        menuEntryDao.getMenuEntryWithFood(menuId)

    suspend fun updateMenuEntry(menuEntry: MenuEntry) {
        menuEntryDao.updateMenuEntry(menuEntry.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun insertMenuEntry(
        restaurantId: String,
        restaurantName: String,
        foodId: String,
        foodName: String,
        foodDescription: String,
        foodImageUrl: String,
        price: Double,
        currency: String = "USD",
        isAvailable: Boolean,
        categoryId: String? = null,
        category: String? = null
    ): String {
        val searchTokens = SearchTokenGenerator.generateTokens(foodName)
        val menuEntry = MenuEntry(
            menuEntryId = "",
            restaurantId = restaurantId,
            restaurantName = restaurantName,
            foodId = foodId,
            foodName = foodName,
            foodDescription = foodDescription,
            foodImageUrl = foodImageUrl,
            price = price,
            currency = currency,
            isAvailable = isAvailable,
            categoryId = categoryId,
            category = category,
            searchTokens = searchTokens.distinct()
        )
        return menuEntryDao.insertMenuEntry(menuEntry)
    }

    suspend fun insertMenuEntry(menuEntry: MenuEntry): String = menuEntryDao.insertMenuEntry(menuEntry)

    suspend fun deleteAll() = menuEntryDao.deleteAll()

    suspend fun searchMenuEntries(query: String): List<MenuEntry> {
        val tokens = SearchTokenGenerator.generateTokensForSearch(query)
        return menuEntryDao.searchByTokens(tokens)
    }

    fun getAllMenuEntries(): Flow<List<MenuEntry>> = menuEntryDao.getAll()

    suspend fun getByRestaurantAndFood(restaurantId: String, foodId: String): MenuEntry? =
        menuEntryDao.getByRestaurantAndFood(restaurantId, foodId)
}