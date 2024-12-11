package com.ams.ultra_iptv.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ams.ultra_iptv.screens.ChannelSelectionScreen
import com.ams.ultra_iptv.screens.HomeScreen
import com.ams.ultra_iptv.screens.LoginScreen
import com.ams.ultra_iptv.screens.PlayerScreen
import com.ams.ultra_iptv.screens.RegisterScreen
import com.ams.ultra_iptv.screens.SplashScreen


@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "splash",
        modifier = modifier
    ) {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("channels/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: "Sin categoría"
            ChannelSelectionScreen(navController, category)
        }
        composable("player/{channel}") { backStackEntry ->
            val channel = backStackEntry.arguments?.getString("channel") ?: "Canal desconocido"
            PlayerScreen(navController, channel)
        }

    }
}

