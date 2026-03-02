package com.example.foodrestaurantdeliveryapp.data.repository.model.food.foodProduct

import com.example.foodrestaurantdeliveryapp.data.entity.food.FoodProduct

sealed class ProductResult {
    object Loading : ProductResult()
    data class Success(val product: FoodProduct) : ProductResult()
    data class Error(val exception: Exception, val message: String) : ProductResult()
    object NoData : ProductResult()
}