package com.example.foodrestaurantdeliveryapp.data.repository.model.food

import com.example.foodrestaurantdeliveryapp.data.dao.food.FoodDao
import com.example.foodrestaurantdeliveryapp.data.entity.food.FoodItem
import com.example.foodrestaurantdeliveryapp.utils.SearchTokenGenerator
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class FoodRepository @Inject constructor(
    private val foodDao: FoodDao
) {
    suspend fun getFoodItem(foodId: String): FoodItem? = foodDao.getFoodItem(foodId)

    suspend fun updateFoodItem(foodItem: FoodItem) {
        foodDao.updateFoodItem(foodItem.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun insertFoodItem(
        categoryId: String,
        categoryName: String,
        name: String,
        description: String,
        imageUrl: String
    ): String {
        val searchTokens = SearchTokenGenerator.generateTokens(name) +
                SearchTokenGenerator.generateTokens(description)
        val foodItem = FoodItem(
            foodId = "",
            categoryId = categoryId,
            categoryName = categoryName,
            name = name,
            nameLowercase = name.lowercase(),
            description = description,
            descriptionLowercase = description.lowercase(),
            imageUrl = imageUrl,
            searchTokens = searchTokens.distinct()
        )
        return foodDao.insert(foodItem)
    }

    suspend fun insertFoodItem(foodItem: FoodItem): String = foodDao.insert(foodItem)

    suspend fun deleteAll() = foodDao.deleteAll()

    suspend fun searchFoodItems(query: String): List<FoodItem> {
        val tokens = SearchTokenGenerator.generateTokensForSearch(query)
        return foodDao.searchByTokens(tokens)
    }

    fun getAllFoodItems(): Flow<List<FoodItem>> = foodDao.getAll()

    suspend fun getFoodByCategory(categoryId: String): List<FoodItem> =
        foodDao.getFoodByCategory(categoryId)
}