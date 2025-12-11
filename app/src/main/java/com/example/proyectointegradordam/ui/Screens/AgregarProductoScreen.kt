package com.example.proyectointegradordam.ui.Screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectointegradordam.data.repository.ProductoRepository
import com.example.proyectointegradordam.ui.components.buttons.PrimaryButton
import com.example.proyectointegradordam.ui.viewmodel.ProductoUiState
import com.example.proyectointegradordam.ui.viewmodel.ProductoViewModel
import com.example.proyectointegradordam.ui.viewmodel.ProductoViewModelFactory
import io.github.jan.supabase.SupabaseClient

@Composable
fun AgregarProductoScreen( // Asegúrate que se llame así para coincidir con tu Navigation
    supabaseClient: SupabaseClient,
    codigoBarrasInicial: String,
    onGuardarExitoso: () -> Unit,
    onCancelar: () -> Unit
) {
    // 1. Instanciar ViewModel
    // Usamos 'remember' para que no se cree el repo cada vez que recompones
    val repository = remember { ProductoRepository() }

    val viewModel: ProductoViewModel = viewModel(
        factory = ProductoViewModelFactory(repository)
    )

    // 2. Estados locales del formulario
    var nombre by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }

    // 3. Observar estado UI del ViewModel
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // 4. Manejo de Eventos (Éxito/Error)
    LaunchedEffect(uiState) {
        when(val state = uiState) {
            // CASO ÉXITO (Mensaje)
            is ProductoUiState.Success -> {
                Toast.makeText(context, state.mensaje, Toast.LENGTH_SHORT).show()
                viewModel.limpiarEstado()
                onGuardarExitoso()
            }
            // CASO ERROR
            is ProductoUiState.Error -> {
                Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
                // Opcional: limpiar estado si quieres borrar el error visualmente
            }
            // Otros estados (Idle, Loading, SuccessLista) no hacen nada aquí
            else -> {}
        }
    }

    // --- UI ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Registrar Nuevo Producto",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Campo: Código de Barras (Solo lectura)
        OutlinedTextField(
            value = codigoBarrasInicial,
            onValueChange = {},
            label = { Text("Código Detectado") },
            readOnly = true,
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo: Nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del Producto") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo: Categoría
        OutlinedTextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoría (Ej. Limpieza)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo: Precio
        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it },
            label = { Text("Precio ($)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo: Stock
        OutlinedTextField(
            value = stock,
            onValueChange = { stock = it },
            label = { Text("Stock Inicial") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botones de Acción
        // Si está cargando, mostramos spinner
        if (uiState is ProductoUiState.Loading) {
            CircularProgressIndicator()
        } else {
            // Botón Guardar
            PrimaryButton(
                text = "Guardar Producto",
                onClick = {
                    viewModel.agregarProducto(
                        nombre = nombre,
                        codigo = codigoBarrasInicial,
                        precioString = precio,
                        categoria = categoria,
                        stockString = stock
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Cancelar
            TextButton(onClick = onCancelar) {
                Text("Cancelar")
            }
        }
    }
}