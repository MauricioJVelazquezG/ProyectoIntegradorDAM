package com.example.proyectointegradordam.ui.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.proyectointegradordam.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.proyectointegradordam.ui.theme.ProyectoIntegradorDAMTheme

@Composable
fun PantallaCargaInicial() {

    val fondo = Color(0xFF4A261C)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logootsowhite),
            contentDescription = "OTSO",
            modifier = Modifier
                .size(250.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaCargaInicial(){

    ProyectoIntegradorDAMTheme()  {
        PantallaCargaInicial()
    }
}

