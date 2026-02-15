package com.example.foodrestaurantdeliveryapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.foodrestaurantdeliveryapp.ui.screen.DetailScreen
import com.example.foodrestaurantdeliveryapp.ui.screen.HomeScreen
import com.example.foodrestaurantdeliveryapp.ui.screen.SettingsScreen

@Composable
fun FoodNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable(route = "home") {
            HomeScreen(
                navigateToDetail = { restaurantId ->
                    navController.navigate("detail/$restaurantId")
                },
                navigateToSettings = { navController.navigate("settings") }
            )
        }

        composable(
            route = "detail/{restaurantId}",
            arguments = listOf(navArgument("restaurantId") { type = NavType.IntType })
        ) {
            DetailScreen(
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(route = "settings") {
            SettingsScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}