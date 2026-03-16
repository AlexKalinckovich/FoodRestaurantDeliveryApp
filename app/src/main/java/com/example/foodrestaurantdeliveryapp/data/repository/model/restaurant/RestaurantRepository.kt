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

    suspend fun searchRestaurantsFuzzy(query: String, allRestaurants: List<Restaurant>): List<Restaurant> {
        val tokenResults = searchRestaurants(query)
        val fuzzyResults = allRestaurants.fuzzySearch(query, threshold = 2)
        return (tokenResults + fuzzyResults).distinctBy { it.restaurantId }
    }

    suspend fun searchByDeliveryFee(maxFee: Double): List<Restaurant> =
        restaurantDao.searchByDeliveryFee(maxFee)

}

fun List<Restaurant>.fuzzySearch(query: String, threshold: Int = 2): List<Restaurant> {
    val queryLower: String = query.lowercase().trim()
    return filter {
        restaurant -> restaurant.nameLowercase.contains(other = queryLower) ||
                      restaurant.addressLowercase.contains(other = queryLower) ||
                      levenshteinDistance(restaurant.nameLowercase, queryLower) <= threshold
    }
}

fun levenshteinDistance(s1: String, s2: String): Int {
    val m: Int = s1.length
    val n: Int = s2.length
    val dp: Array<IntArray> = Array(m + 1) { IntArray(n + 1) }
    for (i in 0..m) {
        dp[i][0] = i
    }
    for (j in 0..n) {
        dp[0][j] = j
    }

    for (i in 1..m) {
        for (j in 1..n) {
            val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1
            dp[i][j] = minOf(
                dp[i - 1][j] + 1,
                dp[i][j - 1] + 1,
                dp[i - 1][j - 1] + cost
            )
        }
    }
    return dp[m][n]
}