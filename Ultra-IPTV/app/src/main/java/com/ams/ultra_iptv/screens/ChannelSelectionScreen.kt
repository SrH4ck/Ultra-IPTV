package com.ams.ultra_iptv.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ams.ultra_iptv.api.ChannelResponse
import com.ams.ultra_iptv.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun ChannelSelectionScreen(navController: NavHostController, category: String) {
    // Estado para el buscador
    var searchQuery by remember { mutableStateOf("") }
    var channels by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    // Obtener los canales para una categoría
    fun fetchChannels() {
        val api = RetrofitClient.instance

        api.getList(category).enqueue(object : Callback<ChannelResponse> {
            override fun onResponse(call: Call<ChannelResponse>, response: Response<ChannelResponse>) {
                isLoading = false
                if (response.isSuccessful) {
                    channels = response.body()?.channels ?: emptyList()
                } else {
                    errorMessage = "Error al obtener los canales"
                }
            }

            override fun onFailure(call: Call<ChannelResponse>, t: Throwable) {
                isLoading = false
                errorMessage = "Error de conexión: ${t.message}"
            }
        })
    }


    // Llamar a fetchChannels al cargar la pantalla
    LaunchedEffect(Unit) {
        fetchChannels()
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
            // Título de la categoría
            Text(
                text = "Lista de Canales",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Buscador de canales
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar canal") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
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
                // Lista de canales
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(channels.filter { it.contains(searchQuery, ignoreCase = true) }) { channel ->
                        ChannelItem(channel = channel) {
                            // Navegar al reproductor con el canal seleccionado
                            navController.navigate("player/$channel")
                        }
                    }
                }
            }
        }
    }
}

// Composable para un elemento de canal
@Composable
fun ChannelItem(channel: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F1F1F) // Fondo del Card
        ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(
            text = channel,
            style = TextStyle(
                color = Color.White,
                fontSize = 18.sp
            ),
            modifier = Modifier
                .padding(16.dp)
        )
    }
}
