package com.example.foodrestaurantdeliveryapp.data.repository


import com.example.foodrestaurantdeliveryapp.data.dao.food.CategoryDao
import com.example.foodrestaurantdeliveryapp.data.entity.food.Category
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {

    fun getAllCategories() : Flow<List<Category>> {
        return categoryDao.getAllCategories();
    }
    suspend fun insertCategories(vararg categories: Category) {
        categories.forEach { categoryDao.insert(category = it) }
    }

    suspend fun isEmpty(): Boolean = categoryDao.getCount() == 0
}