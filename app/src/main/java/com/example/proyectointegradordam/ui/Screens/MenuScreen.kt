package com.example.proyectointegradordam.ui.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow // Usamos LazyRow para que los botones scrolleen si son muchos
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectointegradordam.R
import com.example.proyectointegradordam.data.model.Producto
import com.example.proyectointegradordam.ui.components.ProductoCard
import com.example.proyectointegradordam.ui.components.buttons.BotonFiltro
import com.example.proyectointegradordam.ui.viewmodel.ProductoUiState
import com.example.proyectointegradordam.ui.viewmodel.ProductoViewModel

@Composable
fun Menu(
    viewModel: ProductoViewModel = viewModel() // Inyectamos el ViewModel
) {
    // 1. Cargar datos al entrar a la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarInventario()
    }

    // 2. Observar el estado
    val uiState by viewModel.uiState.collectAsState()

    // Categorías disponibles
    val categorias = listOf(
        "Todo", "Limpieza", "Despensa", "Bebidas",
        "Higiene Personal", "Dulcería", "Panadería"
    )

    var categoriaSeleccionada by remember { mutableStateOf("Todo") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título
        Text(
            text = "Inventario",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Barra de Filtros (Cambiado a LazyRow para evitar errores si hay muchas categorías)
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categorias) { categoria ->
                BotonFiltro(
                    text = categoria,
                    selected = categoriaSeleccionada == categoria,
                    onClick = { categoriaSeleccionada = categoria }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Mostrar contenido según el estado de la BD
        when (val state = uiState) {
            is ProductoUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ProductoUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.error, color = MaterialTheme.colorScheme.error)
                }
            }
            is ProductoUiState.SuccessLista -> {
                // Aquí tenemos la lista REAL de Supabase
                val productos = state.productos

                // Filtramos la lista
                val productosFiltrados = if (categoriaSeleccionada == "Todo") {
                    productos
                } else {
                    productos.filter { it.categoria.equals(categoriaSeleccionada, ignoreCase = true) }
                }

                if (productosFiltrados.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay productos en esta categoría")
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        items(productosFiltrados) { producto ->
                            // IMPORTANTE: Adaptamos el objeto de BD para que funcione con tu ProductoCard
                            // Como tu Card espera una imagen (Int), usamos una función auxiliar
                            ProductoCard(
                                // Nota: Si tu ProductoCard espera un objeto distinto,
                                // tal vez necesites modificar ProductoCard para aceptar el nuevo modelo.
                                // Aquí asumo que ProductoCard acepta el modelo actualizado.
                                producto = producto,
                                imagenRes = obtenerIconoPorCategoria(producto.categoria)
                            )
                        }
                    }
                }
            }
            else -> { /* Estado inicial o idle */ }
        }
    }
}

// Función auxiliar para poner iconos bonitos según la categoría (ya que la BD no tiene R.drawable)
fun obtenerIconoPorCategoria(categoria: String): Int {
    return when (categoria.lowercase()) {
        "limpieza" -> R.drawable.jabon // Asegurate de tener estos drawables o usa uno genérico
        "despensa" -> R.drawable.aceite
        "dulcería", "dulceria" -> R.drawable.chocolate
        "higiene personal" -> R.drawable.papel
        "panadería", "panaderia" -> R.drawable.tortillas
        else -> R.drawable.logootso // Icono por defecto si no coincide
    }
}