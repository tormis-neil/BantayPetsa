package com.bantaypetsa.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bantaypetsa.app.core.designsystem.theme.BantayPetsaTheme
import com.bantaypetsa.app.core.designsystem.theme.BorderSubtle
import com.bantaypetsa.app.core.designsystem.theme.CharcoalGreen
import com.bantaypetsa.app.core.designsystem.theme.PaleGreen
import com.bantaypetsa.app.core.designsystem.theme.PrimaryGreen
import com.bantaypetsa.app.core.designsystem.theme.SoftWhite
import com.bantaypetsa.app.navigation.BantayPetsaBottomBar
import com.bantaypetsa.app.navigation.BantayPetsaNavGraph
import com.bantaypetsa.app.navigation.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Standard window decor (no edge-to-edge) so status bar icons remain clearly visible
        setContent {
            BantayPetsaTheme {
                MainAppScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isBottomNavDestination = Screen.bottomNavItems.any { it.route.substringBefore("?") == currentDestination?.route?.substringBefore("?") }
    val isSettingsDestination = currentDestination?.route == Screen.Settings.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = SoftWhite,
        topBar = {
            if (isSettingsDestination) {
                Column {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Settings",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = CharcoalGreen,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = { navController.popBackStack() },
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .size(40.dp)
                                    
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = PrimaryGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = SoftWhite,
                            titleContentColor = CharcoalGreen,
                            navigationIconContentColor = PrimaryGreen
                        )
                    )
                    HorizontalDivider(thickness = 0.8.dp, color = BorderSubtle)
                }
            }
        },
        bottomBar = {
            if (isBottomNavDestination) {
                BantayPetsaBottomBar(
                    navController = navController,
                    currentDestination = currentDestination
                )
            }
        }
    ) { innerPadding ->
        BantayPetsaNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
