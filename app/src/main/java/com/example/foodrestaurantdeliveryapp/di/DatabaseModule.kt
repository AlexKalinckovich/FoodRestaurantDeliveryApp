package com.example.foodrestaurantdeliveryapp.di

import android.content.Context
import androidx.room.Room
import com.example.foodrestaurantdeliveryapp.data.FoodDatabase
import com.example.foodrestaurantdeliveryapp.data.dao.food.CategoryDao
import com.example.foodrestaurantdeliveryapp.data.dao.food.FoodDao
import com.example.foodrestaurantdeliveryapp.data.dao.menu.MenuEntryDao
import com.example.foodrestaurantdeliveryapp.data.dao.restaurant.RestaurantDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFoodDatabase(
        @ApplicationContext context: Context
    ): FoodDatabase {
        return Room.databaseBuilder(
            context,
            FoodDatabase::class.java,
            "food_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRestaurantDao(database: FoodDatabase): RestaurantDao {
        return database.restaurantDao()
    }

    @Provides
    @Singleton
    fun provideMenuDao(database: FoodDatabase): MenuEntryDao {
        return database.menuDao()
    }

    @Provides
    @Singleton
    fun provideFoodDao(database: FoodDatabase): FoodDao {
        return database.foodDao()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(database: FoodDatabase): CategoryDao {
        return database.categoryDao()
    }
}