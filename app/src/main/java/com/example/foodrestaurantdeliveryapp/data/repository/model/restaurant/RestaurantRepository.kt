package com.example.foodrestaurantdeliveryapp.data.repository.model.restaurant

import com.example.foodrestaurantdeliveryapp.data.dao.menu.MenuEntryDao
import com.example.foodrestaurantdeliveryapp.data.dao.restaurant.RestaurantDao
import com.example.foodrestaurantdeliveryapp.data.entity.restaurant.Restaurant
import com.example.foodrestaurantdeliveryapp.data.repository.model.menu.model.MenuWithDetails
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class RestaurantRepository @Inject constructor(
    private val restaurantDao: RestaurantDao,
    private val menuDao: MenuEntryDao
) {
    fun getAllRestaurants(): Flow<List<Restaurant>> {
        return restaurantDao.getAllRestaurants()
    }
    fun getRestaurantStream(id: Int): Flow<Restaurant?> {
        return restaurantDao.getRestaurantById(id)
    }

    fun getMenuForRestaurant(restaurantId: Int): Flow<List<MenuWithDetails>> {
        return menuDao.getMenuForRestaurant(restaurantId)
    }

    suspend fun insertRestaurant(restaurant: Restaurant): Long = restaurantDao.insert(restaurant)

    suspend fun deleteRestaurant(restaurant: Restaurant) {
        restaurantDao.delete(restaurant)
    }

    suspend fun updateRestaurant(restaurant: Restaurant) {
        restaurantDao.update(restaurant)
    }

    suspend fun deleteAll() {
        restaurantDao.deleteAll()
    }
}