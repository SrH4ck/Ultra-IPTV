package com.ams.ultra_iptv.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ams.ultra_iptv.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RegisterScreen(navController: NavHostController) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Hombre") } // Valor inicial del género
    var receiveNotifications by remember { mutableStateOf(false) } // Estado para el switch
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Firebase Firestore instance
    val firestore = FirebaseFirestore.getInstance()

    // Scroll state
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.Green)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState) // Habilitar scroll vertical
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(200.dp)
                )

                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    text = "Regístrate",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Campo de nombre
                OutlinedTextField(
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                    ),
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre completo") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    singleLine = true
                )

                // Campo de correo
                OutlinedTextField(
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                    ),
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    singleLine = true
                )

                // Campo de contraseña
                OutlinedTextField(
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                    ),
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    singleLine = true
                )

                // Campo de confirmar contraseña
                OutlinedTextField(
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                    ),
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar contraseña") },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // RadioGroup para seleccionar género
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Género:",
                        color = Color.White,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = gender == "Hombre",
                            onClick = { gender = "Hombre" },
                            colors = RadioButtonDefaults.colors(selectedColor = Color.Green)
                        )
                        Text(text = "Hombre", color = Color.White, fontSize = 16.sp)

                        Spacer(modifier = Modifier.width(16.dp))

                        RadioButton(
                            selected = gender == "Mujer",
                            onClick = { gender = "Mujer" },
                            colors = RadioButtonDefaults.colors(selectedColor = Color.Green)
                        )
                        Text(text = "Mujer", color = Color.White, fontSize = 16.sp)

                        Spacer(modifier = Modifier.width(16.dp))

                        RadioButton(
                            selected = gender == "Otro",
                            onClick = { gender = "Otro" },
                            colors = RadioButtonDefaults.colors(selectedColor = Color.Green)
                        )
                        Text(text = "Otro", color = Color.White, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Switch para notificaciones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Recibir notificaciones por correo",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Switch(
                        checked = receiveNotifications,
                        onCheckedChange = { receiveNotifications = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.Green)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de registro
                Button(
                    onClick = {
                        if (password == confirmPassword && email.isNotEmpty() && password.length >= 6) {
                            isLoading = true
                            registerUser(email, password) { success, message ->
                                isLoading = false
                                if (success) {
                                    saveUserData(firestore, name, email, gender, receiveNotifications)
                                    navController.navigate("login")
                                } else {
                                    errorMessage = message ?: "Error desconocido"
                                }
                            }
                        } else {
                            errorMessage = "Verifica los campos y asegúrate de que las contraseñas coincidan."
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF58CF0F))
                ) {
                    Text(
                        text = "Registrarse",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    )
                }

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Enlace para volver al login
                Text(
                    text = "¿Ya tienes cuenta? Inicia sesión",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable {
                            navController.navigate("login")
                        }
                )
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

// Función para guardar datos en Firebase Firestore
fun saveUserData(
    firestore: FirebaseFirestore,
    name: String,
    email: String,
    gender: String,
    receiveNotifications: Boolean
) {
    val userData = hashMapOf(
        "name" to name,
        "email" to email,
        "gender" to gender,
        "receiveNotifications" to receiveNotifications
    )

    firestore.collection("users")
        .add(userData)
        .addOnSuccessListener { Log.d("RegisterScreen", "Usuario guardado en Firestore") }
        .addOnFailureListener { e -> Log.e("RegisterScreen", "Error al guardar usuario", e) }
}
fun registerUser(
    email: String,
    password: String,
    callback: (Boolean, String?) -> Unit
) {
    FirebaseAuth.getInstance()
        .createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, null) // Registro exitoso
            } else {
                callback(false, task.exception?.message) // Error en el registro
            }
        }
}