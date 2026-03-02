package com.example.foodrestaurantdeliveryapp.data.api.dto

import com.example.foodrestaurantdeliveryapp.data.entity.food.FoodProduct

fun OpenFoodFactsResponse.toEntity(): FoodProduct? {
    val product = this.product ?: return null

    fun Double?.validateCalories() = this?.takeIf { it >= 0 }

    return FoodProduct(
        barcode = this.code,
        name = product.productName ?: "Unknown",
        brand = product.brands,
        calories = product.nutriments?.energyKcal.validateCalories(),
        ingredients = product.ingredientsText,
        imageUrl = product.imageUrl,
        lastUpdated = System.currentTimeMillis()
    )
}