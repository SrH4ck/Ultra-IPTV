package com.ams.ultra_iptv

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs

import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension


@Composable
fun LoginScreen(navController: NavHostController) {
    // Estados para los campos y mensajes
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Función de autenticación usando Firebase
    fun login() {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            isLoading = true
            signInUser(email, password) { success, message ->
                isLoading = false
                if (success) {
                    navController.navigate("home") // Navegar a la pantalla de home
                } else {
                    errorMessage = message ?: "Error desconocido"
                }
            }
        } else {
            errorMessage = "Por favor, completa todos los campos."
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.Green, modifier = Modifier.align(Alignment.Center))
        } else {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Crear referencias para los componentes
                val (
                    logo, title, emailField, passwordField, loginButton,
                    registerText, errorText
                ) = createRefs()

                // Logo
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .constrainAs(logo) {
                            top.linkTo(parent.top, margin = 48.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .size(250.dp) // Especifica el tamaño deseado directamente
                )

                // Título
                Text(
                    text = "Iniciar sesión",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.constrainAs(title) {
                        top.linkTo(logo.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )

                // Campo de correo
                OutlinedTextField(
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp
                    ),
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier
                        .constrainAs(emailField) {
                            top.linkTo(title.bottom, margin = 32.dp) // Margen superior
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints // Asegurar que respeta las restricciones laterales
                        }
                        .padding(horizontal = 24.dp) // Margen lateral adicional
                        .fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    )
                )

                // Campo de contraseña
                OutlinedTextField(
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp
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
                        .constrainAs(passwordField) {
                            top.linkTo(emailField.bottom, margin = 16.dp) // Margen superior entre campos
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints // Asegurar que respeta las restricciones laterales
                        }
                        .padding(horizontal = 24.dp) // Margen lateral adicional
                        .fillMaxWidth(),
                    singleLine = true
                )

                // Botón de inicio de sesión
                Button(
                    onClick = { login() },
                    modifier = Modifier
                        .constrainAs(loginButton) {
                            top.linkTo(passwordField.bottom, margin = 24.dp) // Margen superior
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.fillToConstraints // Asegurar que respeta las restricciones laterales
                        }
                        .padding(horizontal = 24.dp) // Margen lateral adicional
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF58CF0F))
                ) {
                    Text(
                        text = "Iniciar sesión",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    )
                }

                // Mensaje de error
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.constrainAs(errorText) {
                            top.linkTo(loginButton.bottom, margin = 8.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )
                }

                // Enlace para registrarse
                Text(
                    text = "¿No tienes cuenta? Regístrate",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier
                        .clickable {
                            navController.navigate("register")
                        }
                        .constrainAs(registerText) {
                            top.linkTo(loginButton.bottom, margin = 24.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
            }
        }
    }
}

// Función para iniciar sesión en Firebase
fun signInUser(
    email: String,
    password: String,
    callback: (Boolean, String?) -> Unit
) {
    FirebaseAuth.getInstance()
        .signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, null) // Inicio de sesión exitoso
            } else {
                callback(false, task.exception?.message) // Error en el inicio de sesión
            }
        }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController())
}


