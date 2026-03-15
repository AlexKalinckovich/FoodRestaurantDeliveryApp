package com.example.foodrestaurantdeliveryapp.data.repository.model.category

import com.example.foodrestaurantdeliveryapp.data.dao.food.CategoryDao
import com.example.foodrestaurantdeliveryapp.data.entity.food.Category
import com.example.foodrestaurantdeliveryapp.utils.SearchTokenGenerator
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {
    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()

    suspend fun getCategoryById(categoryId: String): Category? =
        categoryDao.getCategoryById(categoryId)

    suspend fun insertCategory(name: String): String {
        val category = Category(
            categoryId = "",
            name = name,
            nameLowercase = name.lowercase()
        )
        return categoryDao.insert(category)
    }

    suspend fun insertCategory(category: Category): String = categoryDao.insert(category)

    suspend fun searchCategories(query: String): List<Category> {
        val tokens = SearchTokenGenerator.generateTokensForSearch(query)
        return categoryDao.searchByTokens(tokens)
    }

    suspend fun deleteAll() = categoryDao.deleteAll()
}