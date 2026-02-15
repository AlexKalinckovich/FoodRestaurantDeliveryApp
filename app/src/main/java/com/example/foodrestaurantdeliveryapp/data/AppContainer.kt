package com.example.foodrestaurantdeliveryapp.data

import android.content.Context
import com.example.foodrestaurantdeliveryapp.data.repository.RestaurantRepository

interface AppContainer {
    val restaurantRepository: RestaurantRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    private val database: FoodDatabase by lazy {
        FoodDatabase.getDatabase(context)
    }

    override val restaurantRepository: RestaurantRepository by lazy {
        RestaurantRepository(
            restaurantDao = database.restaurantDao(),
            menuDao = database.menuDao()
        )
    }
}