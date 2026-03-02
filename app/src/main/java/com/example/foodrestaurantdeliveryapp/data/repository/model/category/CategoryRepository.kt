package com.example.foodrestaurantdeliveryapp.data.repository.model.category

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
    suspend fun insertCategory(category: Category): Long = categoryDao.insert(category)

    suspend fun isEmpty(): Boolean = categoryDao.getCount() == 0

    suspend fun deleteAll() {
        categoryDao.deleteAll()
    }
}