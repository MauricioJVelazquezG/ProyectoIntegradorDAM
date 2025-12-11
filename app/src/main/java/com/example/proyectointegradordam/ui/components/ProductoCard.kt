package com.example.proyectointegradordam.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectointegradordam.data.model.Producto

@Composable
fun ProductoCard(
    producto: Producto,
    imagenRes: Int // <--- AGREGAMOS ESTE PARÁMETRO
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp), // Ajusta la altura según tu diseño
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. IMAGEN (Usamos el imagenRes que recibimos)
            Image(
                painter = painterResource(id = imagenRes),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(16.dp))

            // 2. DATOS DEL PRODUCTO
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = producto.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = producto.categoria, // Mostramos la categoría
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // 3. PRECIO Y STOCK
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                // Convertimos el Double a String con signo de pesos
                Text(
                    text = "$${producto.precio}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF4CAF50) // Verde dinero
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Convertimos el Int a String
                Text(
                    text = "${producto.stock} pz",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}