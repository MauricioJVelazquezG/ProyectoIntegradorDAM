package com.example.proyectointegradordam.ui.Screens

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
import java.text.Normalizer

@Composable
fun Menu(
    supabaseClient: SupabaseClient,
    onBack: () -> Unit
) {
    val repository = remember { ProductoRepository() }
    val viewModel: ProductoViewModel = viewModel(
        factory = ProductoViewModelFactory(repository)
    )

    LaunchedEffect(Unit) {
        viewModel.cargarInventario()
    }

    val uiState by viewModel.uiState.collectAsState()

    // Categorías (Puedes dejarlas con acentos para que se vean bonitas en la UI)
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
        // --- HEADER ---
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
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Inventario",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(48.dp))
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

        // --- LISTA DE PRODUCTOS ---
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

                // --- LOGICA DE FILTRADO CORREGIDA ---
                val productosFiltrados = if (categoriaSeleccionada == "Todo") {
                    productos
                } else {
                    productos.filter { producto ->
                        // Normalizamos ambos lados para ignorar acentos, mayúsculas y espacios
                        val catBD = normalizarTexto(producto.categoria)
                        val catSel = normalizarTexto(categoriaSeleccionada)
                        catBD == catSel
                    }
                }

                if (productosFiltrados.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "No hay productos en: $categoriaSeleccionada",
                                color = Color.Gray
                            )
                            // Útil para depurar: Muestra qué categorías hay realmente
                            /*
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Categorías encontradas en BD:", fontSize = 10.sp)
                            productos.map { it.categoria }.distinct().forEach {
                                Text(it, fontSize = 10.sp)
                            }
                            */
                        }
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
            else -> {}
        }
    }
}

// --- FUNCIONES AUXILIARES ---

// 1. Quitar acentos, espacios y minúsculas
fun normalizarTexto(texto: String): String {
    val sinAcentos = Normalizer.normalize(texto, Normalizer.Form.NFD)
        .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
    return sinAcentos.lowercase().trim()
}

// 2. Obtener Icono (También usa normalización para ser seguro)
fun obtenerIconoPorCategoria(categoria: String): Int {
    val catNormalizada = normalizarTexto(categoria)

    return when {
        catNormalizada.contains("limpieza") -> R.drawable.cloro
        catNormalizada.contains("despensa") -> R.drawable.aceite
        catNormalizada.contains("dulce") -> R.drawable.chicles // Coincide con dulceria o dulcería
        catNormalizada.contains("higiene") -> R.drawable.papel
        catNormalizada.contains("pan") -> R.drawable.tortillas
        catNormalizada.contains("bebida") -> R.drawable.logootso
        else -> R.drawable.logootso
    }
}