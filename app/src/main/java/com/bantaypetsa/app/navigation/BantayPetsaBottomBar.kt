package com.bantaypetsa.app.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.bantaypetsa.app.core.designsystem.theme.BorderSubtle
import com.bantaypetsa.app.core.designsystem.theme.MutedGreen
import com.bantaypetsa.app.core.designsystem.theme.PaleGreen
import com.bantaypetsa.app.core.designsystem.theme.PrimaryGreen
import com.bantaypetsa.app.core.designsystem.theme.SurfaceWhite

@Composable
fun BantayPetsaBottomBar(
    navController: NavHostController,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider(thickness = 0.8.dp, color = BorderSubtle)
        NavigationBar(
            containerColor = SurfaceWhite,
            tonalElevation = 0.dp
        ) {
            Screen.bottomNavItems.forEach { screen ->
                val selected = currentDestination?.hierarchy?.any { it.route?.substringBefore("?") == screen.route.substringBefore("?") } == true

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        if (!selected) {
                            val isNavigatingToHome = screen.route == Screen.Home.route
                            val targetRoute = if (screen == Screen.Alerts) Screen.Alerts.createRoute("All") else screen.route
                            navController.navigate(targetRoute) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = !isNavigatingToHome
                                    inclusive = isNavigatingToHome
                                }
                                launchSingleTop = true
                                restoreState = !isNavigatingToHome
                            }
                        }
                    },
                    icon = {
                        val icon = if (selected) screen.selectedIcon else screen.unselectedIcon
                        if (icon != null) {
                            Icon(
                                imageVector = icon,
                                contentDescription = screen.title
                            )
                        }
                    },
                    label = {
                        Text(
                            text = screen.title,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryGreen,
                        selectedTextColor = PrimaryGreen,
                        indicatorColor = PaleGreen,
                        unselectedIconColor = MutedGreen,
                        unselectedTextColor = MutedGreen
                    )
                )
            }
        }
    }
}
