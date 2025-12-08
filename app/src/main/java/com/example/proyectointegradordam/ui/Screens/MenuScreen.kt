package com.example.proyectointegradordam.ui.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.proyectointegradordam.data.model.Producto
import com.example.proyectointegradordam.ui.components.ProductoCard
import com.example.proyectointegradordam.ui.components.buttons.BotonFiltro
import com.example.proyectointegradordam.R


@Composable
fun Menu() {

    val categorias = listOf(
        "Todo", "Limpieza", "Despensa", "Bebidas",
        "Higiene Personal", "Dulcería", "Panadería"
    )

    val productos = listOf(
        Producto(
            id = 1,
            nombre = "Tortillas de harina",
            precio = "34",
            cantidad = "24 pz",
            codigo = "5901234123457",
            categoria = "Despensa",
            imagen = R.drawable.tortillas,
        ),
        Producto(
            id = 2,
            nombre = "Chocolate",
            precio = "34",
            cantidad = "14 pz",
            codigo = "5901234123458",
            categoria = "Dulcería",
            imagen = R.drawable.chocolate,
        ),
        Producto(
            id = 3,
            nombre = "Aceite",
            precio = "34",
            cantidad = "7 pz",
            codigo = "5901234123459",
            categoria = "Despensa",
            imagen = R.drawable.aceite,
        ),
        Producto(
            id = 4,
            nombre = "Papel higiénico",
            precio = "34",
            cantidad = "18 pz",
            codigo = "5901234123460",
            categoria = "Higiene Personal",
            imagen = R.drawable.papel,
        )
    )

    var categoriaSeleccionada by remember { mutableStateOf("Todo") }

    Column (
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            categorias.forEach { categoria ->
                BotonFiltro (
                    text = categoria,
                    selected = categoriaSeleccionada == categoria,
                    onClick = { categoriaSeleccionada = categoria }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val productosFiltrados =
            if (categoriaSeleccionada == "Todo") productos
            else productos.filter { it.categoria == categoriaSeleccionada }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(productosFiltrados) { producto ->
                ProductoCard(producto)
            }
        }
    }
}
