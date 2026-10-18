package com.bantaypetsa.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bantaypetsa.app.feature.alerts.AlertsScreen
import com.bantaypetsa.app.feature.auth.AuthScreen
import com.bantaypetsa.app.feature.batches.AddEditBatchScreen
import com.bantaypetsa.app.feature.batches.RecordStockActionScreen
import com.bantaypetsa.app.feature.dashboard.DashboardScreen
import com.bantaypetsa.app.feature.history.HistoryScreen
import com.bantaypetsa.app.feature.products.AddEditProductScreen
import com.bantaypetsa.app.feature.products.ProductDetailScreen
import com.bantaypetsa.app.feature.products.ProductsScreen
import com.bantaypetsa.app.feature.profile.ProfileScreen
import com.bantaypetsa.app.feature.settings.SettingsScreen
import com.bantaypetsa.app.feature.splash.SplashScreen

@Composable
fun BantayPetsaNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.SignIn.route) {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignIn.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            DashboardScreen(
                onNavigateToProducts = {
                    navController.navigate(Screen.Products.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToAlerts = { filter ->
                    navController.navigate(Screen.Alerts.createRoute(filter)) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = false
                    }
                },
                onNavigateToAddProduct = { navController.navigate(Screen.AddEditProduct.createRoute()) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToProductDetail = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                }
            )
        }

        composable(Screen.Products.route) {
            ProductsScreen(
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onAddProductClick = {
                    navController.navigate(Screen.AddEditProduct.createRoute())
                }
            )
        }

        composable(
            route = Screen.Alerts.route,
            arguments = listOf(navArgument("filter") {
                type = NavType.StringType
                nullable = true
                defaultValue = "All"
            })
        ) { backStackEntry ->
            val filter = backStackEntry.arguments?.getString("filter") ?: "All"
            AlertsScreen(
                initialFilter = filter,
                onRecordStockAction = { batchId ->
                    navController.navigate(Screen.RecordStockAction.createRoute(batchId))
                }
            )
        }

        composable(Screen.History.route) {
            HistoryScreen()
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onSignOut = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }

        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(
                productId = productId,
                onNavigateBack = { navController.popBackStack() },
                onEditProduct = { prodId ->
                    navController.navigate(Screen.AddEditProduct.createRoute(prodId))
                },
                onAddBatch = { prodId ->
                    navController.navigate(Screen.AddEditBatch.createRoute(prodId))
                },
                onEditBatch = { prodId, batchId ->
                    navController.navigate(Screen.AddEditBatch.createRoute(prodId, batchId))
                },
                onRecordStockAction = { batchId ->
                    navController.navigate(Screen.RecordStockAction.createRoute(batchId))
                }
            )
        }

        composable(
            route = Screen.AddEditProduct.route,
            arguments = listOf(navArgument("productId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            AddEditProductScreen(
                productId = productId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.AddEditBatch.route,
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType },
                navArgument("batchId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            val batchId = backStackEntry.arguments?.getString("batchId")
            AddEditBatchScreen(
                productId = productId,
                batchId = batchId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.RecordStockAction.route,
            arguments = listOf(navArgument("batchId") { type = NavType.StringType })
        ) { backStackEntry ->
            val batchId = backStackEntry.arguments?.getString("batchId") ?: ""
            RecordStockActionScreen(
                batchId = batchId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
