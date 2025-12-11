package com.example.proyectointegradordam.ui.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectointegradordam.R
import com.example.proyectointegradordam.ui.components.buttons.PrimaryButton
import com.example.proyectointegradordam.ui.components.texts.Link
import com.example.proyectointegradordam.ui.components.texts.TextField
import com.example.proyectointegradordam.ui.theme.ProyectoIntegradorDAMTheme
import com.example.proyectointegradordam.ui.viewmodel.AuthUiState
import com.example.proyectointegradordam.ui.viewmodel.LoginViewModel
import com.example.proyectointegradordam.ui.viewmodel.LoginViewModelFactory
import io.github.jan.supabase.SupabaseClient

@Composable
fun LoginScreen(
    supabaseClient: SupabaseClient, // <--- NECESARIO: Recibir cliente
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    // Inicializamos el ViewModel con el Factory para pasarle Supabase
    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(supabaseClient)
    )

    // Estados locales para los campos de texto
    val correo = remember { mutableStateOf("") }
    val contra = remember { mutableStateOf("") }

    // Observamos el estado global de la autenticación
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Efecto para manejar el resultado (Éxito o Error)
    LaunchedEffect(uiState) {
        when(val state = uiState) {
            is AuthUiState.Success -> {
                Toast.makeText(context, "Bienvenido", Toast.LENGTH_SHORT).show()
                onLoginSuccess()
                viewModel.resetState()
            }
            is AuthUiState.Error -> {
                Toast.makeText(context, state.mensaje, Toast.LENGTH_LONG).show()
                viewModel.resetState() // Opcional: limpiar error después del Toast
            }
            else -> {}
        }
    }

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
                modifier = Modifier.size(width = 150.dp, height = 80.dp)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text("Ingresa tu cuenta", color = Color(0xFF3C2E2A))
        TextField(value = correo, label = "Email")

        Spacer(modifier = Modifier.height(14.dp))

        Text("Ingresa tu contraseña", color = Color(0xFF3C2E2A))
        // Asumo que tu componente maneja internamente la visualización de password
        TextField(value = contra, label = "Contraseña")

        Spacer(modifier = Modifier.height(25.dp))

        // Mostrar indicador de carga si se está logueando
        if (uiState is AuthUiState.Loading) {
            CircularProgressIndicator(color = Color(0xFF3C2E2A))
        } else {
            PrimaryButton(
                text = "Iniciar sesión",
                onClick = {
                    viewModel.iniciarSesion(correo.value, contra.value)
                }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))


    }
}