package com.example.foodrestaurantdeliveryapp.ui.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodrestaurantdeliveryapp.data.entity.food.Category
import com.example.foodrestaurantdeliveryapp.data.entity.food.FoodItem
import com.example.foodrestaurantdeliveryapp.data.entity.menu.MenuEntry
import com.example.foodrestaurantdeliveryapp.data.repository.model.category.CategoryRepository
import com.example.foodrestaurantdeliveryapp.data.repository.model.food.FoodRepository
import com.example.foodrestaurantdeliveryapp.data.repository.model.menu.MenuEntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuEntryEditViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val menuEntryRepository: MenuEntryRepository,
    private val categoryRepository: CategoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val NO_ID = -1
    private val restaurantId: Int = savedStateHandle["restaurantId"] ?: NO_ID
    private val menuId: Int = savedStateHandle["menuId"] ?: NO_ID

    private val _uiState = MutableStateFlow(MenuEntryEditUiState())
    val uiState: StateFlow<MenuEntryEditUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            loadCategories()
            when {
                menuId != NO_ID -> loadForEdit()
                restaurantId != NO_ID -> setupAddMode()
                else -> setInvalidArgumentsError()
            }
        }
    }

    private suspend fun loadCategories() {
        val categories: List<Category> = categoryRepository.getAllCategories().first()
        _uiState.update { it.copy(categories = categories) }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun updateDescription(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun updateImageUrl(url: String) {
        _uiState.update { it.copy(imageUrl = url) }
    }

    fun updatePrice(price: String) {
        _uiState.update { it.copy(price = price) }
    }

    fun toggleAvailable() {
        _uiState.update { it.copy(isAvailable = !it.isAvailable) }
    }

    fun selectCategory(categoryId: Int) {
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun save(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (!validate()) return onError("Please fill all required fields")

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                when (_uiState.value.mode) {
                    MenuEntryEditUiState.Mode.ADD -> performAdd()
                    MenuEntryEditUiState.Mode.EDIT -> performEdit()
                }
                _uiState.update { it.copy(isSaving = false) }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false) }
                onError("Save failed: ${e.message}")
            }
        }
    }

    private suspend fun loadForEdit() {
        _uiState.update { it.copy(isLoading = true) }
        try {
            val entry = menuEntryRepository.getMenuEntryWithFood(menuId)
            if (entry != null) {
                _uiState.update {
                    it.copy(
                        mode = MenuEntryEditUiState.Mode.EDIT,
                        menuId = menuId,
                        name = entry.foodItem.name,
                        description = entry.foodItem.description,
                        imageUrl = entry.foodItem.imageUrl,
                        price = entry.menuEntry.price,
                        isAvailable = entry.menuEntry.isAvailable,
                        selectedCategoryId = entry.foodItem.categoryId,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(errorMessage = "Entry not found", isLoading = false) }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(errorMessage = "Failed to load data", isLoading = false) }
        }
    }

    private fun setupAddMode() {
        _uiState.update {
            it.copy(
                mode = MenuEntryEditUiState.Mode.ADD,
                restaurantId = restaurantId,
                isLoading = false
            )
        }
    }

    private fun setInvalidArgumentsError() {
        _uiState.update { it.copy(errorMessage = "Invalid arguments", isLoading = false) }
    }

    private fun validate(): Boolean {
        val state = _uiState.value
        val errors = mutableMapOf<String, String>()
        if (state.name.isBlank()) errors["name"] = "Name is required"
        if (state.price.isBlank()) errors["price"] = "Price is required"
        if (state.selectedCategoryId == null) errors["category"] = "Category is required"
        _uiState.update { it.copy(validationErrors = errors) }
        return errors.isEmpty()
    }

    private suspend fun performAdd() {
        val state = _uiState.value
        val restaurantId = requireNotNull(state.restaurantId) { "Restaurant ID missing" }
        val categoryId = requireNotNull(state.selectedCategoryId) { "Category not selected" }

        val foodItem = FoodItem(
            categoryId = categoryId,
            name = state.name,
            description = state.description,
            imageUrl = state.imageUrl
        )
        val foodId = foodRepository.insertFoodItem(foodItem).toInt()

        val menuEntry = MenuEntry(
            restaurantId = restaurantId,
            foodId = foodId,
            price = state.price,
            isAvailable = state.isAvailable
        )
        menuEntryRepository.insertMenuEntry(menuEntry)
    }

    private suspend fun performEdit() {
        val state = _uiState.value
        val menuId = requireNotNull(state.menuId) { "Menu ID missing" }
        val categoryId = requireNotNull(state.selectedCategoryId) { "Category not selected" }

        val existing = requireNotNull(menuEntryRepository.getMenuEntryWithFood(menuId)) {
            "Menu entry not found"
        }

        val updatedFood = existing.foodItem.copy(
            categoryId = categoryId,
            name = state.name,
            description = state.description,
            imageUrl = state.imageUrl
        )
        foodRepository.updateFoodItem(updatedFood)

        val updatedMenu = existing.menuEntry.copy(
            price = state.price,
            isAvailable = state.isAvailable
        )
        menuEntryRepository.updateMenuEntry(updatedMenu)
    }
}

data class MenuEntryEditUiState(
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val price: String = "",
    val isAvailable: Boolean = true,
    val mode: Mode = Mode.ADD,
    val restaurantId: Int? = null,
    val menuId: Int? = null,
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: Int? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val validationErrors: Map<String, String> = emptyMap()
) {
    enum class Mode { ADD, EDIT }
    val isAddMode: Boolean get() = mode == Mode.ADD
    val isEditMode: Boolean get() = mode == Mode.EDIT
}