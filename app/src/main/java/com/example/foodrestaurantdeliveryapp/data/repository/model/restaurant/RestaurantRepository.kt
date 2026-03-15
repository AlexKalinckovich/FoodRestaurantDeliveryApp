package com.example.foodrestaurantdeliveryapp.data.repository.model.restaurant

import com.example.foodrestaurantdeliveryapp.data.dao.menu.MenuEntryDao
import com.example.foodrestaurantdeliveryapp.data.dao.restaurant.RestaurantDao
import com.example.foodrestaurantdeliveryapp.data.entity.restaurant.Restaurant
import com.example.foodrestaurantdeliveryapp.data.repository.model.menu.model.MenuWithDetails
import com.example.foodrestaurantdeliveryapp.utils.SearchTokenGenerator
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class RestaurantRepository @Inject constructor(
    private val restaurantDao: RestaurantDao,
    private val menuDao: MenuEntryDao
) {
    fun getAllRestaurants(): Flow<List<Restaurant>> = restaurantDao.getAllRestaurants()

    fun getRestaurantById(id: String): Flow<Restaurant?> = restaurantDao.getRestaurantById(id)

    fun getMenuForRestaurant(restaurantId: String): Flow<List<MenuWithDetails>> =
        menuDao.getMenuForRestaurant(restaurantId)

    suspend fun insertRestaurant(
        name: String,
        address: String,
        deliveryFee: Double,
        imageUrl: String
    ): String {
        val searchTokens = SearchTokenGenerator.generateTokens(name) +
                SearchTokenGenerator.generateTokens(address)
        val restaurant = Restaurant(
            restaurantId = "",
            name = name,
            nameLowercase = name.lowercase(),
            address = address,
            addressLowercase = address.lowercase(),
            deliveryFee = deliveryFee,
            imageUrl = imageUrl,
            searchTokens = searchTokens.distinct()
        )
        return restaurantDao.insert(restaurant)
    }

    suspend fun insertRestaurant(restaurant: Restaurant): String = restaurantDao.insert(restaurant)

    suspend fun updateRestaurant(restaurant: Restaurant) {
        restaurantDao.update(restaurant.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun deleteRestaurant(restaurant: Restaurant) {
        restaurantDao.delete(restaurant)
    }

    suspend fun deleteAll() {
        restaurantDao.deleteAll()
    }

    suspend fun searchRestaurants(query: String): List<Restaurant> {
        val tokens = SearchTokenGenerator.generateTokensForSearch(query)
        return restaurantDao.searchByTokens(tokens)
    }

    suspend fun searchByDeliveryFee(maxFee: Double): List<Restaurant> =
        restaurantDao.searchByDeliveryFee(maxFee)
}