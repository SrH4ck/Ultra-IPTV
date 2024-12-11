package com.ams.ultra_iptv.api

import android.content.Context
import com.ams.ultra_iptv.AppContextProvider
import com.ams.ultra_iptv.R
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.X509TrustManager
import java.security.SecureRandom
import java.io.InputStream

object RetrofitClient {

    // Cambia la IP a la correcta
    private const val BASE_URL = "https://192.168.166.16:5000" // Asegúrate de usar HTTPS

    val instance: ServerApi by lazy {
        val client = createOkHttpClient(AppContextProvider.appContext)

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServerApi::class.java)
    }

    // Función para crear el OkHttpClient con un certificado autofirmado
    private fun createOkHttpClient(context: Context): OkHttpClient {
        val trustManager = createTrustManager(context)

        // Configura el SSLContext con el certificado
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(trustManager), SecureRandom())

        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .hostnameVerifier { _, _ -> true }  // Aceptar cualquier nombre de host
            .build()
    }

    // Función para crear un TrustManager que use el certificado autofirmado
    private fun createTrustManager(context: Context): X509TrustManager {
        val certificateFactory = CertificateFactory.getInstance("X.509")

        // Aquí, usamos el archivo de certificado que agregaste a `res/raw/server.crt`
        val certInputStream: InputStream = context.resources.openRawResource(R.raw.server)

        val certificate = certificateFactory.generateCertificate(certInputStream)
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null, null)
        keyStore.setCertificateEntry("server", certificate)

        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(keyStore)

        return trustManagerFactory.trustManagers[0] as X509TrustManager
    }
}


/*object RetrofitClient {
    // Para el emulador
    private const val BASE_URLEmulador = "http://10.0.2.16:5000"  // Usa esta dirección en lugar de 192.168.202.16 en el emulador

    private const val BASE_URL = "https://192.168.166.16:5000" // Cambia la IP a la nueva dirección


    val instance: ServerApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServerApi::class.java)
    }
}*/
