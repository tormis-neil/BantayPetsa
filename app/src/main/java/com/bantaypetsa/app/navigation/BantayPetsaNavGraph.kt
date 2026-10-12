package com.bantaypetsa.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bantaypetsa.app.feature.alerts.AlertsScreen
import com.bantaypetsa.app.feature.dashboard.DashboardScreen
import com.bantaypetsa.app.feature.history.HistoryScreen
import com.bantaypetsa.app.feature.products.ProductsScreen
import com.bantaypetsa.app.feature.settings.SettingsScreen

@Composable
fun BantayPetsaNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            DashboardScreen(
                onNavigateToProducts = { navController.navigate(Screen.Products.route) },
                onNavigateToAlerts = { navController.navigate(Screen.Alerts.route) }
            )
        }
        composable(Screen.Products.route) {
            ProductsScreen()
        }
        composable(Screen.Alerts.route) {
            AlertsScreen()
        }
        composable(Screen.History.route) {
            HistoryScreen()
        }
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}
