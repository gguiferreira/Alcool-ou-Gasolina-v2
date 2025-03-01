package com.example.gasolinaoualcool.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gasolinaoualcool.GasolinaOuAlcoolScreen
import com.example.gasolinaoualcool.ui.screens.FuelListScreen
import com.example.gasolinaoualcool.ui.screens.FuelDetailScreen

@Composable
fun NavGraph(navController: NavHostController, context: Context) {
    NavHost(navController, startDestination = "home") {
        composable("home") {
            GasolinaOuAlcoolScreen(context) {
                navController.navigate("fuel_list")
            }
        }
        composable("fuel_list") {
            FuelListScreen(context, navController)
        }
        composable("fuel_detail/{stationId}") { backStackEntry ->
            val stationId = backStackEntry.arguments?.getString("stationId")
            stationId?.let {
                FuelDetailScreen(navController, it, context)
            }
        }
    }
}
