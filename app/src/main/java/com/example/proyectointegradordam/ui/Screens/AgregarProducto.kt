package com.example.proyectointegradordam.ui.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.proyectointegradordam.data.model.Producto
import com.example.proyectointegradordam.ui.components.buttons.PrimaryButton

@Composable
fun AgregarProducto(producto : Producto) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Agregar producto",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(value = nombre, label = "Ingrese nombre del producto" )

        TextField(value = categoria,label = "Ingresa la categoría del producto",)

        TextField(value = precio, label = "Ingresa el precio del producto")

        TextField(value = stock, label = "Stock inicial")

        //PrimaryButton(text = "Guardar producto")

        Spacer(modifier = Modifier.height(8.dp))

        PrimaryButton(
            text = "Volver a escanear",
            onClick = {  }
        )
    }
}
