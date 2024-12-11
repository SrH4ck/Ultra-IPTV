package com.ams.ultra_iptv.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

// Respuesta del servidor para la lista de canales
data class ChannelResponse(
    val channels: List<String>
)


data class CategoryResponse(
    val categories: List<String> // La lista de categorías
)


// Interfaz Retrofit para las solicitudes al servidor
interface ServerApi {

    @GET("/api/get-channels/{category}")
    fun getList(@Path("category") category: String): Call<ChannelResponse>


    @GET("/api/get-list/{filename}")
    fun getChannelM3U(@Path("filename") filename: String): Call<ResponseBody>  // Usamos ResponseBody para manejar el archivo M3U

    // Endpoint para obtener las categorías
    @GET("/api/get-categories")
    fun getCategories(): Call<CategoryResponse>


}
