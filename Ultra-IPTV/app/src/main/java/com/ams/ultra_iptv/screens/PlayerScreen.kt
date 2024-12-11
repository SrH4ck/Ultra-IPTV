package com.ams.ultra_iptv.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.ams.ultra_iptv.api.RetrofitClient
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody

@SuppressLint("RememberReturnType")
@Composable
fun PlayerScreen(navController: NavHostController, filename: String) {
    val context = LocalContext.current
    val libVLC = remember { LibVLC(context) }
    val mediaPlayer = remember { MediaPlayer(libVLC) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    var videoUrl by remember { mutableStateOf("") }
    var currentVolume by remember { mutableStateOf(100f) } // Volumen inicial al máximo
    var showExitDialog by remember { mutableStateOf(false) } // Controla la visibilidad del Dialog

    // Descargar el archivo M3U
    fun fetchM3UFile(vlcVideoLayout: VLCVideoLayout) {
        if (videoUrl.isNotEmpty()) return

        val api = RetrofitClient.instance

        api.getChannelM3U("$filename.m3u").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                isLoading = false
                if (response.isSuccessful) {
                    val m3uContent = response.body()?.string() ?: ""
                    val urls = parseM3U(m3uContent)
                    if (urls.isNotEmpty()) {
                        videoUrl = urls.first()
                        Log.d("Player", "URL proporcionada a VLC: $videoUrl")
                        startPlayerWithUrl(videoUrl, vlcVideoLayout)
                    } else {
                        errorMessage = "No hay URLs válidas en el archivo M3U."
                    }
                } else {
                    errorMessage = "Error al obtener el archivo M3U."
                }
            }

            private fun startPlayerWithUrl(videoUrl: String, vlcVideoLayout: VLCVideoLayout) {
                if (videoUrl.isNotEmpty()) {
                    Log.d("Player", "URL del video: $videoUrl")
                    val media = Media(libVLC, Uri.parse(videoUrl))
                    media.addOption(":network-caching=3000")
                    mediaPlayer.media = media
                    mediaPlayer.detachViews()
                    mediaPlayer.attachViews(vlcVideoLayout, null, false, false)
                    mediaPlayer.volume = currentVolume.toInt()
                    mediaPlayer.play()
                    Log.d("Player", "Reproducción iniciada")
                } else {
                    Log.e("Player", "La URL del video está vacía")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                isLoading = false
                errorMessage = "Error de conexión: ${t.message}"
            }
        })
    }

    mediaPlayer.setEventListener { event ->
        when (event.type) {
            MediaPlayer.Event.Playing -> Log.d("Player", "El reproductor está reproduciendo")
            MediaPlayer.Event.EndReached -> Log.d("Player", "Fin del video")
            MediaPlayer.Event.Buffering -> Log.d("Player", "Buffering...")
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.stop()
            mediaPlayer.release()
            libVLC.release()
        }
    }

    // Interceptar el botón de retroceso para mostrar el diálogo
    BackHandler {
        showExitDialog = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidView(
            factory = { context ->
                VLCVideoLayout(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            update = { vlcVideoLayout ->
                fetchM3UFile(vlcVideoLayout)
            },
            modifier = Modifier.fillMaxSize()
        )

        if (isLoading) {
            CircularProgressIndicator(color = Color.Green, modifier = Modifier.align(Alignment.Center))
        } else if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                style = TextStyle(color = Color.White, fontSize = 18.sp),
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Reproduciendo: $filename",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    IconButton(onClick = { showExitDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.White
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Volumen",
                        tint = Color.White
                    )
                    Slider(
                        value = currentVolume,
                        onValueChange = { newVolume ->
                            currentVolume = newVolume
                            mediaPlayer.volume = newVolume.toInt()
                        },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = Color.Green
                        )
                    )
                }
            }
        }
    }

    // Dialog para confirmar la salida
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Confirmación") },
            text = { Text("¿Está seguro de que desea salir del canal?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        navController.popBackStack() // Salir y volver atrás
                    }
                ) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}

// Función para parsear el contenido M3U
fun parseM3U(content: String): List<String> {
    return content.lines()
        .filter { it.startsWith("http") }
        .map { it.trim() }
}


