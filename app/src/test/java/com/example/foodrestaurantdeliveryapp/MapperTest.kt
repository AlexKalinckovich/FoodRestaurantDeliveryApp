package com.example.foodrestaurantdeliveryapp

import com.example.foodrestaurantdeliveryapp.data.api.dto.NutrimentsDto
import com.example.foodrestaurantdeliveryapp.data.api.dto.OpenFoodFactsResponse
import com.example.foodrestaurantdeliveryapp.data.api.dto.ProductDto
import com.example.foodrestaurantdeliveryapp.data.api.dto.toEntity
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MapperTest {
    
    @Test
    fun `mapper handles decimal calories correctly`() {
        
        val response = OpenFoodFactsResponse(
            code = "123456",
            product = ProductDto(
                productName = "Test Product",
                nutriments = NutrimentsDto(energyKcal = 3333.3333)
            ),
            statusVerbose = "ok"
        )

        
        val entity = response.toEntity()

        
        assertEquals(3333.3333, entity?.calories)  
        assertEquals("Test Product", entity?.name)
    }

    @Test
    fun `mapper handles null calories safely`() {
        
        val response = OpenFoodFactsResponse(
            code = "123456",
            product = ProductDto(
                productName = "Test Product",
                nutriments = NutrimentsDto(energyKcal = null)
            ),
            statusVerbose = "ok"
        )

        val entity = response.toEntity()

        assertEquals(null, entity?.calories)  
    }

    @Test
    fun `mapper handles invalid calories safely`() {
        
        val response = OpenFoodFactsResponse(
            code = "123456",
            product = ProductDto(
                productName = "Test Product",
                nutriments = NutrimentsDto(energyKcal = -100.0)
            ),
            statusVerbose = "ok"
        )

        val entity = response.toEntity()

        assertEquals(null, entity?.calories)  
    }
}