package com.ams.ultra_iptv

import android.app.Application
import com.google.firebase.FirebaseApp

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Inicializar Firebase
        FirebaseApp.initializeApp(this)

        // Aquí puedes inicializar cualquier otra configuración global.
    }
}