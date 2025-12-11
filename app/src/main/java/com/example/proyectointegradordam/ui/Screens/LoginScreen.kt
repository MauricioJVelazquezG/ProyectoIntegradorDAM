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

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(), // 1. Inyectamos el ViewModel
    onLoginSuccess: () -> Unit, // 2. Callback para navegar al Home cuando sea exitoso
    onNavigateToRegister: () -> Unit // 3. Callback para ir a registro
) {
    // Estados locales para los campos de texto
    // Nota: Asumo que tu componente custom TextField usa MutableState, si no, adáptalo a String simple
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
                onLoginSuccess() // Navega a la siguiente pantalla
                viewModel.resetState()
            }
            is AuthUiState.Error -> {
                Toast.makeText(context, state.mensaje, Toast.LENGTH_LONG).show()
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
        // IMPORTANTE: Asegúrate de que tu componente TextField maneje inputType password visualmente
        TextField(value = contra, label = "Contraseña")

        Spacer(modifier = Modifier.height(25.dp))

        // Mostrar indicador de carga si se está logueando
        if (uiState is AuthUiState.Loading) {
            CircularProgressIndicator(color = Color(0xFF3C2E2A))
        } else {
            // AQUI ESTA LA CONEXIÓN QUE FALTABA:
            PrimaryButton(
                text = "Iniciar sesión",
                onClick = {
                    viewModel.iniciarSesion(correo.value, contra.value)
                }
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "¿No tienes cuenta? ",
            color = Color(0xFF3C2E2A)
        )

        Link (
            text = "Registrate",
            color = Color(0xFF3C2E2A),
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(bottom = 20.dp)
                .align(Alignment.CenterHorizontally),
            onClick = { onNavigateToRegister() } // Conectamos la navegación
        )
    }
}

// Preview actualizado (mockeando callbacks vacíos para diseño)
@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen(){
    ProyectoIntegradorDAMTheme {
        LoginScreen(
            onLoginSuccess = {},
            onNavigateToRegister = {}
        )
    }
}