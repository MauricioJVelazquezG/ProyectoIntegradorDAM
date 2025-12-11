package com.example.proyectointegradordam.ui.Screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectointegradordam.data.model.Producto
import com.example.proyectointegradordam.ui.components.buttons.PrimaryButton
import com.example.proyectointegradordam.ui.viewmodel.ProductoUiState
import com.example.proyectointegradordam.ui.viewmodel.ProductoViewModel

@Composable
fun AgregarProducto(
    productoEscaneado: Producto, // El objeto que viene del escáner (con el código de barras)
    viewModel: ProductoViewModel = viewModel(), // Inyección automática del ViewModel
    onNavigateBack: () -> Unit // Callback para volver
) {
    // 1. Definir estados locales para los campos del formulario
    // Inicializamos con los datos del producto escaneado si existen
    var nombre by remember { mutableStateOf(productoEscaneado.nombre) }
    var categoria by remember { mutableStateOf("") } // Nota: Asegúrate de tener este campo en tu BD si lo vas a usar
    var precio by remember { mutableStateOf(if (productoEscaneado.precio > 0) productoEscaneado.precio.toString() else "") }
    var stock by remember { mutableStateOf("") }     // Nota: Asegúrate de tener este campo en tu BD

    // 2. Observar el estado del ViewModel (Carga, Éxito, Error)
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // 3. Reaccionar a eventos (Éxito al guardar)
    LaunchedEffect(uiState) {
        if (uiState is ProductoUiState.Success) {
            Toast.makeText(context, (uiState as ProductoUiState.Success).mensaje, Toast.LENGTH_SHORT).show()
            viewModel.limpiarEstado()
            onNavigateBack() // Regresa a la pantalla anterior automáticamente
        }
        if (uiState is ProductoUiState.Error) {
            Toast.makeText(context, (uiState as ProductoUiState.Error).error, Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Agregar producto",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Muestra el código de barras detectado (Solo lectura)
        OutlinedTextField(
            value = productoEscaneado.codigoBarras,
            onValueChange = {},
            label = { Text("Código de Barras Detectado") },
            readOnly = true,
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo Nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Ingrese nombre del producto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo Categoría
        OutlinedTextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Ingresa la categoría") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo Precio (Teclado numérico)
        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it },
            label = { Text("Precio") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo Stock (Teclado numérico)
        OutlinedTextField(
            value = stock,
            onValueChange = { stock = it },
            label = { Text("Stock inicial") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Guardar
        if (uiState is ProductoUiState.Loading) {
            CircularProgressIndicator()
        } else {
            PrimaryButton(
                text = "Guardar producto",
                onClick = {
                    // Llamamos a la función del ViewModel definida anteriormente
                    viewModel.agregarProducto(
                        nombre = nombre,
                        codigo = productoEscaneado.codigoBarras, // Usamos el código escaneado
                        precioString = precio
                        // Nota: Si agregas stock y categoría al ViewModel, pásalos aquí también
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón Cancelar / Volver
        TextButton(onClick = onNavigateBack) {
            Text("Volver a escanear")
        }
    }
}