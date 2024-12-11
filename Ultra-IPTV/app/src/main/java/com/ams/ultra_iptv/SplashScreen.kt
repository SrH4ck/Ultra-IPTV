package com.ams.ultra_iptv

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import androidx.compose.ui.draw.alpha

@Composable
fun SplashScreen(navController: NavHostController) {
    // Estados para la animación
    var startAnimation by remember { mutableStateOf(false) }

    // Escalado animado
    val scale = animateFloatAsState(
        targetValue = if (startAnimation) 1.9f else 1f, // Escala de 1 a 1.5
        animationSpec = tween(durationMillis = 3000) // Duración: 1.5 segundos
    )

    // Opacidad animada
    val alpha = animateFloatAsState(
        targetValue = if (startAnimation) 0f else 1f, // Se desvanece al final
        animationSpec = tween(durationMillis = 3000)
    )

    LaunchedEffect(Unit) {
        startAnimation = true // Inicia la animación
        delay(2000) // Espera el tiempo de la animación
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true } // Elimina la pantalla de Splash del stack
        }
    }

    // Diseño de la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.logo), // Reemplazar con el recurso del logo
            contentDescription = "Logo",
            modifier = Modifier
                .scale(scale.value) // Aplicar la escala animada
                .alpha(alpha.value) // Aplicar la opacidad animada
        )
    }
}
