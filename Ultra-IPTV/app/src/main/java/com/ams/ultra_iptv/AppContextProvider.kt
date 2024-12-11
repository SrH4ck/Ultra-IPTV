package com.ams.ultra_iptv

import android.content.Context
object AppContextProvider {
    lateinit var appContext: Context

    // Este método debe ser llamado en el onCreate de la actividad principal
    fun initialize(context: Context) {
        appContext = context.applicationContext // Guardamos el contexto de la aplicación
    }
}

