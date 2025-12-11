package com.example.proyectointegradordam.ui.Screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectointegradordam.data.model.Producto
import com.example.proyectointegradordam.data.repository.ProductoRepository
import com.example.proyectointegradordam.ui.components.buttons.PrimaryButton
import com.example.proyectointegradordam.ui.viewmodel.ProductDetailViewModel
import com.example.proyectointegradordam.ui.viewmodel.ProductDetailViewModelFactory

@Composable
fun ProductoEncontradoScreen(
    producto: Producto,
    onBackToScan: () -> Unit
) {
    // Instanciamos el repositorio (es barato crear esta clase)
    val repository = remember { ProductoRepository() }

    val viewModel: ProductDetailViewModel = viewModel(
        factory = ProductDetailViewModelFactory(repository, producto)
    )
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            Toast.makeText(context, "Stock actualizado correctamente", Toast.LENGTH_SHORT).show()
            viewModel.resetSuccessState()
            onBackToScan()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()), // Scroll por si la descripción es larga
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Detalle del Producto",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 20.dp, top = 10.dp)
        )

        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Información Principal
                DetailRow("Nombre:", producto.nombre)
                DetailRow("Código:", producto.codigoBarras) // Usando tu variable codigoBarras
                DetailRow("Categoría:", producto.categoria)
                DetailRow("Precio:", "$${producto.precio}")

                // Descripción (si existe)
                producto.descripcion?.let { desc ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Descripción:", fontWeight = FontWeight.SemiBold)
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                // Control de Stock
                Text(
                    text = "Control de Inventario",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    FilledIconButton(
                        onClick = { viewModel.decrementStock() },
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Restar", tint = MaterialTheme.colorScheme.onErrorContainer)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = uiState.currentStock.toString(),
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                        Text("unidades", style = MaterialTheme.typography.bodySmall)
                    }

                    FilledIconButton(
                        onClick = { viewModel.incrementStock() },
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Sumar", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (uiState.error != null) {
            Text(
                text = uiState.error!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        if (uiState.isSaving) {
            CircularProgressIndicator()
        } else {
            PrimaryButton(
                text = "Actualizar Stock",
                onClick = { viewModel.saveChanges() }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onBackToScan,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.SemiBold)
        Text(text = value, maxLines = 1, modifier = Modifier.weight(1f, fill = false)) // Evita que texto muy largo rompa el layout
    }
}