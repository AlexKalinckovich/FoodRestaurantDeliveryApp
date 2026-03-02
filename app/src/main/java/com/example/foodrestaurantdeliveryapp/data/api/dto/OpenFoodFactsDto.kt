package com.example.foodrestaurantdeliveryapp.data.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenFoodFactsResponse(
    val code: String,
    val product: ProductDto? = null,
    @SerialName("status_verbose")
    val statusVerbose: String
)

@Serializable
data class ProductDto(
    @SerialName("product_name")
    val productName: String? = null,
    val brands: String? = null,
    val nutriments: NutrimentsDto? = null,
    @SerialName("ingredients_text")
    val ingredientsText: String? = null,
    @SerialName("image_url")
    val imageUrl: String? = null
)

@Serializable
data class NutrimentsDto(
    @SerialName("energy-kcal_100g")
    val energyKcal: Double? = null
)