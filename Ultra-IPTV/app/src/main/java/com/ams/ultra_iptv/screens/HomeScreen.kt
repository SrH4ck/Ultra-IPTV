package com.ams.ultra_iptv.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ams.ultra_iptv.api.CategoryResponse
import com.ams.ultra_iptv.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun HomeScreen(navController: NavHostController) {
    // Estado para las categorías, el texto del buscador y el estado de carga
    var categories by remember { mutableStateOf<List<String>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    // Función para obtener las categorías del servidor
    fun fetchCategories() {
        val api = RetrofitClient.instance

        // Llamar a la API para obtener las categorías
        api.getCategories().enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                isLoading = false // Actualizar estado de carga

                if (response.isSuccessful) {
                    categories = response.body()?.categories ?: emptyList() // Obtener la lista de categorías
                } else {
                    errorMessage = "Error al obtener las categorías"
                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                isLoading = false // Actualizar estado de carga en caso de error
                errorMessage = "Error de conexión: ${t.message}"
            }
        })
    }

    // Llamar a fetchCategories al cargar la pantalla
    LaunchedEffect(Unit) {
        fetchCategories()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = "Categorías",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Buscador
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = {
                    Text(
                        text = "Buscar categoría",
                        color = Color.White // Cambiar el color del texto del label
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search, // Icono de lupa
                        contentDescription = "Buscar",
                        tint = Color.White
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textStyle = TextStyle(color = Color.White),
                singleLine = true
            )

            // Mostrar mensaje de error
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Si se está cargando, mostrar un indicador de progreso
            if (isLoading) {
                CircularProgressIndicator(color = Color.Green)
            } else {
                // Lista de categorías
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories.filter { it.contains(searchQuery, ignoreCase = true) }) { category ->
                        CategoryItem(category = category) {
                            // Navegar a la pantalla de selección de canales
                            navController.navigate("channels/$category")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(category: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF58CF0F) // Fondo del Card
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(
            text = category,
            style = TextStyle(
                color = Color.White,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .padding(16.dp)
        )
    }
    Spacer(modifier = Modifier.height(12.dp)) // espacio entre los elementos de la lista
}
// antonio.iesgerena@gmail.com   import androidx.compose.ui.platform.LocalContext