package com.example.foodrestaurantdeliveryapp.ui.view_model
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrestaurantdeliveryapp.data.entity.restaurant.Restaurant
import com.example.foodrestaurantdeliveryapp.data.repository.model.menu.model.MenuWithDetails
import com.example.foodrestaurantdeliveryapp.data.repository.model.restaurant.RestaurantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val restaurantRepository: RestaurantRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val restaurantId: Int = savedStateHandle["restaurantId"] ?: -1
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        if (restaurantId != -1) {

            viewModelScope.launch {
                restaurantRepository.getRestaurantStream(restaurantId).collect { restaurant ->
                    _uiState.update { it.copy(restaurant = restaurant) }
                }
            }

            viewModelScope.launch {
                restaurantRepository.getMenuForRestaurant(restaurantId).collect { menu ->
                    _uiState.update { it.copy(menuItems = menu) }
                }
            }
        }
    }
}

data class DetailUiState(
    val restaurant: Restaurant? = null,
    val menuItems: List<MenuWithDetails> = emptyList()
)