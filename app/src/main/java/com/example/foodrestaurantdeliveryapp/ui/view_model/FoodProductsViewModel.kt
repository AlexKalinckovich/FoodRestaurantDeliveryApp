package com.example.foodrestaurantdeliveryapp.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrestaurantdeliveryapp.NetworkMonitor
import com.example.foodrestaurantdeliveryapp.data.entity.food.FoodProduct
import com.example.foodrestaurantdeliveryapp.data.repository.model.food.foodProduct.FoodProductRepository
import com.example.foodrestaurantdeliveryapp.data.repository.model.food.foodProduct.ProductResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodProductsViewModel @Inject constructor(
    private val repository: FoodProductRepository,
    val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _uiState = MutableStateFlow<FoodProductUiState>(FoodProductUiState())
    private var networkCollectionJob: Job? = null
    private var productCollectionJob: Job? = null
    val uiState: StateFlow<FoodProductUiState> = _uiState.asStateFlow()

    init {
        networkCollectionJob = viewModelScope.launch {
            networkMonitor.isConnected.collect { isConnected ->
                _uiState.update {
                    it.copy(isOffline = !isConnected)
                }
            }
        }
    }

    fun searchProduct(barcode: String) {
        productCollectionJob?.cancel()
        productCollectionJob = viewModelScope.launch {
            repository.getProductFlow(barcode)
                .catch { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Unknown error"
                        )
                    }
                }
                .collect { result ->
                    handleProductResult(result)
                }
        }
    }

    private fun handleProductResult(result: ProductResult) {
        _uiState.update { currentState ->
            when (result) {
                is ProductResult.Loading -> currentState.copy(isLoading = true)
                is ProductResult.Success -> currentState.copy(
                    product = result.product,
                    isLoading = false,
                    error = null
                )
                is ProductResult.Error -> currentState.copy(
                    isLoading = false,
                    error = result.message
                )
                is ProductResult.NoData -> currentState.copy(
                    isLoading = false,
                    error = "Product not found"
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        productCollectionJob?.cancel()
        networkCollectionJob?.cancel()
    }
}


data class FoodProductUiState(
    val product: FoodProduct? = null,
    val isLoading: Boolean = false,
    val isOffline: Boolean = false,
    val error: String? = null,
    val barcode: String = ""  
)