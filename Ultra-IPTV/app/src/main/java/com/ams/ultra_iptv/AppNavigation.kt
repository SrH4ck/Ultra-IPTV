package com.ams.ultra_iptv

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


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

