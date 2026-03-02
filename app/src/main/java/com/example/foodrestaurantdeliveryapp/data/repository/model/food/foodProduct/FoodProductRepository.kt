package com.example.foodrestaurantdeliveryapp.data.repository.model.food.foodProduct

import com.example.foodrestaurantdeliveryapp.NetworkMonitor
import com.example.foodrestaurantdeliveryapp.data.api.FoodProductApi
import com.example.foodrestaurantdeliveryapp.data.api.dto.toEntity
import com.example.foodrestaurantdeliveryapp.data.dao.food.FoodProductDao
import com.example.foodrestaurantdeliveryapp.data.entity.food.FoodProduct
import com.example.foodrestaurantdeliveryapp.exceptions.OfflineApiCallException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FoodProductRepository @Inject constructor(
    private val api: FoodProductApi,
    private val dao: FoodProductDao,
    private val networkMonitor: NetworkMonitor,
) {

    companion object {
        private const val CACHE_MAX_AGE_MS = 24 * 60 * 60 * 1000L
        private const val TAG = "FoodProductRepository"
    }

    suspend fun deleteAll(){
        dao.deleteOldEntries(cutoffTime = System.currentTimeMillis())
    }
    fun getProductFlow(barcode: String): Flow<ProductResult> = flow {
        emit(value = ProductResult.Loading)

        val cachedProduct: FoodProduct? = dao.getProduct(barcode)

        if (cachedProduct != null) {
            emit(value = ProductResult.Success(cachedProduct))
        }


        if (networkMonitor.isConnected.value && shouldRefreshData(cachedProduct)) {
            suspend fun apiGetProductInfoByBarcode() {
                dao.deleteOldEntries(cutoffTime = System.currentTimeMillis() - CACHE_MAX_AGE_MS)
                try {
                    val freshProduct = api.fetchProduct(barcode).toEntity()

                    if (freshProduct != null) {
                        dao.insert(freshProduct)
                        emit(value = ProductResult.Success(freshProduct))
                    } else {
                        emit(value = ProductResult.NoData)
                    }
                } catch (exception: Exception) {
                    println("$TAG Failed to fetch product: $barcode; exception: $exception")
                    emit(
                        value = ProductResult.Error(
                            exception, message = exception.message ?: "Unknown error"
                        )
                    )
                }
            }
            apiGetProductInfoByBarcode()
        }else if(cachedProduct == null){
            val errorString = "Offline"
            emit(
                value = ProductResult.Error(
                    exception = OfflineApiCallException(message = errorString),
                    message   = errorString,
                )
            )
        }
    }.flowOn(context = Dispatchers.IO)

    private fun shouldRefreshData(product: FoodProduct?): Boolean {
        return product == null || isCacheStale(product)
    }

    private fun isCacheStale(product: FoodProduct): Boolean {
        val cacheAge = System.currentTimeMillis() - product.lastUpdated
        return cacheAge > CACHE_MAX_AGE_MS
    }
}