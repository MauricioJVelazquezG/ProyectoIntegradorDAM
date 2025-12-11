package com.example.proyectointegradordam.ui.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectointegradordam.R
import com.example.proyectointegradordam.ui.viewmodel.DecisionesViewModel
import com.example.proyectointegradordam.ui.viewmodel.DecisionesUiState

@Composable
fun Decisiones(
    viewModel: DecisionesViewModel = viewModel(),
    onIrEscanear: () -> Unit = {},
    onIrInventario: () -> Unit = {}
) {

    // Observamos el estado del ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // ----- Manejo de navegación -----
    when (uiState) {
        is DecisionesUiState.IrEscanear -> {
            onIrEscanear()
            viewModel.limpiarEstado()
        }

        is DecisionesUiState.IrInventario -> {
            onIrInventario()
            viewModel.limpiarEstado()
        }

        else -> {}
    }

    // ---- UI ----
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.TopCenter
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(30.dp),
            modifier = Modifier.padding(top = 200.dp)
        ) {

            Text(
                text = "Selecciona una opción",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            // -------------------------
            // BOTÓN ESCANEAR
            // -------------------------
            Box(
                modifier = Modifier
                    .width(280.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFFF936E))
                    .clickable { viewModel.seleccionarEscanear() },
                contentAlignment = Alignment.CenterStart
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    modifier = Modifier.padding(20.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.escanearje),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )

                    Text(
                        text = "Escanear\nproducto",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF63200F)
                    )
                }
            }

            // -------------------------
            // BOTÓN INVENTARIO
            // -------------------------
            Box(
                modifier = Modifier
                    .width(280.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFCCE8F8))
                    .clickable { viewModel.seleccionarInventario() },
                contentAlignment = Alignment.CenterStart
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                    modifier = Modifier.padding(20.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.inventarioje),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )

                    Text(
                        text = "Ver\nInventario",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF63200F)
                    )
                }
            }
        }
    }
}


