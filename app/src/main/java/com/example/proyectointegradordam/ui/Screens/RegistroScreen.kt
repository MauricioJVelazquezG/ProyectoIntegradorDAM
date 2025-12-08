package com.example.proyectointegradordam.ui.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyectointegradordam.R
import com.example.proyectointegradordam.ui.components.texts.Link
import com.example.proyectointegradordam.ui.components.texts.TextField
import com.example.proyectointegradordam.ui.theme.ProyectoIntegradorDAMTheme

@Composable
fun RegistroScreen(onLoginClick: () -> Unit) {

    val correo = remember { mutableStateOf("") }
    val contra = remember { mutableStateOf("") }
    val contraConfirmar = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box{
            Image(
                painter = painterResource(R.drawable.logootso),
                contentDescription = "Otso",
                modifier = Modifier
                    .size(width = 150.dp, height = 80.dp)
            )
        }


        Spacer(modifier = Modifier.height(40.dp))

        Text("Crear tu cuenta", color = Color(0xFF3C2E2A))

        TextField(value = correo, label = "Correo")
        Spacer(modifier = Modifier.height(12.dp))

        Text("Ingresa tu contraseña", color = Color(0xFF3C2E2A))
        TextField(value = contra, label = "Contraseña")
        Spacer(modifier = Modifier.height(12.dp))

        Text("Confirma tu contraseña", color = Color(0xFF3C2E2A))
        TextField(value = contraConfirmar, label = "Contraseña")

        Spacer(modifier = Modifier.height(25.dp))

        //PrimaryButton(text = "Iniciar sesión") { }//NO SUPE HACER ESTA CONEXION, AYUDAAAAAAAAA

        Spacer(modifier = Modifier.height(30.dp))

        Link (
            text = "¿Ya tienes cuenta? Inicia sesión",
            color = Color(0xFF3C2E2A)
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegistroScreen(){

    ProyectoIntegradorDAMTheme()  {
        RegistroScreen() { }
    }
}
