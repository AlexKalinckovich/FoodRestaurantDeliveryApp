package com.example.foodrestaurantdeliveryapp.di

import com.example.foodrestaurantdeliveryapp.data.dao.menu.MenuEntryDao
import com.example.foodrestaurantdeliveryapp.data.repository.RestaurantRepository
import com.example.foodrestaurantdeliveryapp.data.dao.restaurant.RestaurantDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRestaurantRepository(
        restaurantDao: RestaurantDao,
        menuDao: MenuEntryDao
    ): RestaurantRepository {
        return RestaurantRepository(restaurantDao, menuDao)
    }
}