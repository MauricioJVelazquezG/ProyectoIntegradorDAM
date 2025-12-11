package com.example.proyectointegradordam.ui.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectointegradordam.R
import com.example.proyectointegradordam.data.repository.ProductoRepository
import com.example.proyectointegradordam.ui.components.ProductoCard
import com.example.proyectointegradordam.ui.components.buttons.BotonFiltro
import com.example.proyectointegradordam.ui.viewmodel.ProductoUiState
import com.example.proyectointegradordam.ui.viewmodel.ProductoViewModel
import com.example.proyectointegradordam.ui.viewmodel.ProductoViewModelFactory
import io.github.jan.supabase.SupabaseClient

@Composable
fun Menu(
    supabaseClient: SupabaseClient, // Recibimos el cliente para instanciar el repo
    onBack: () -> Unit // Callback para volver a la pantalla anterior
) {
    // 1. Configuramos el ViewModel con su Factory y Repositorio
    // Usamos 'remember' para que no se cree el repo en cada recomposición
    val repository = remember { ProductoRepository() }
    val viewModel: ProductoViewModel = viewModel(
        factory = ProductoViewModelFactory(repository)
    )

    // 2. Cargar datos al entrar a la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarInventario()
    }

    // 3. Observar el estado
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
        // --- HEADER CON BOTÓN ATRÁS ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Regresar",
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Empuja el texto al centro (opcional)

            Text(
                text = "Inventario",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.weight(1f)) // Balancea el espacio
            Spacer(modifier = Modifier.width(48.dp)) // Espacio equivalente al botón para centrar exacto
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- BARRA DE FILTROS ---
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

        // --- CONTENIDO SEGÚN ESTADO ---
        when (val state = uiState) {
            is ProductoUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is ProductoUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Ocurrió un error", color = MaterialTheme.colorScheme.error)
                        Text(text = state.error, fontSize = 12.sp)
                    }
                }
            }
            is ProductoUiState.SuccessLista -> {
                val productos = state.productos

                // Filtramos la lista localmente
                val productosFiltrados = if (categoriaSeleccionada == "Todo") {
                    productos
                } else {
                    productos.filter {
                        it.categoria.equals(categoriaSeleccionada, ignoreCase = true)
                    }
                }

                if (productosFiltrados.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No hay productos en: $categoriaSeleccionada",
                            color = Color.Gray
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(productosFiltrados) { producto ->
                            ProductoCard(
                                producto = producto,
                                imagenRes = obtenerIconoPorCategoria(producto.categoria)
                            )
                        }
                    }
                }
            }
            else -> {
                // Estado inicial o vacío
            }
        }
    }
}

// Función auxiliar para iconos
fun obtenerIconoPorCategoria(categoria: String): Int {
    return when (categoria.lowercase()) {
        "limpieza" -> R.drawable.cloro
        "despensa" -> R.drawable.aceite
        "dulcería", "dulceria" -> R.drawable.chicles
        "higiene personal" -> R.drawable.papel
        "panadería", "panaderia" -> R.drawable.tortillas
        "bebidas" -> R.drawable.logootso // Asumiendo que tienes un icono para bebidas o usas el logo
        else -> R.drawable.logootso
    }
}