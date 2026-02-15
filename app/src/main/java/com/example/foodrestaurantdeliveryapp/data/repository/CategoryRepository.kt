package com.example.foodrestaurantdeliveryapp.data.repository


import com.example.foodrestaurantdeliveryapp.data.dao.food.CategoryDao
import com.example.foodrestaurantdeliveryapp.data.entity.food.Category
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {
    suspend fun insertCategories(vararg categories: Category) {
        categories.forEach { categoryDao.insert(category = it) }
    }

    suspend fun isEmpty(): Boolean = categoryDao.getCount() == 0
}