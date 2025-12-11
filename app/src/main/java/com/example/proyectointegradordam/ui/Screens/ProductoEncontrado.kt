package com.example.proyectointegradordam.ui.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyectointegradordam.data.model.Producto
import com.example.proyectointegradordam.ui.components.buttons.PrimaryButton
import com.example.proyectointegradordam.ui.theme.ProyectoIntegradorDAMTheme


@Composable
fun ProductoEncontrado(producto: Producto, onIncrement: () -> Unit, onDecrement: () -> Unit) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Producto encontrado",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(text = "Nombre: ${producto.nombre}")
        Text(text = "Codigo: ${producto.codigo}")
        Text(text = "Precio: $${producto.precio}")
        Text(text = "Stock: $${producto.cantidad}")

        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            IconButton(onClick = onDecrement) {
                Icon(imageVector = Icons.Default.Clear,
                    contentDescription = "Quitar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onIncrement) {
                Icon(imageVector = Icons.Default.Add,
                    contentDescription = "Sumar")
            }
        }

        PrimaryButton(
            text = "Guardar producto",
            onClick = {  }
        )

        Spacer(modifier = Modifier.height(8.dp))

        PrimaryButton(
            text = "Volver a escanear",
            onClick = {  }
        )
    }
}
