package com.example.foodrestaurantdeliveryapp.data.api

import com.example.foodrestaurantdeliveryapp.data.api.dto.OpenFoodFactsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface FoodProductApi {
    @GET("api/v0/product/{barcode}.json")
    suspend fun fetchProduct(@Path("barcode") barcode: String): OpenFoodFactsResponse
}