package com.example.proyectointegradordam.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BotonFiltro(text: String, selected: Boolean, onClick: () -> Unit) {
    Box (
        modifier = Modifier
            .background(
                color = if (selected) Color(0xFFFFE6D9) else Color(0xFFF0F0F0),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable() { onClick() }
    ) {
        Text(text, fontSize = 12.sp)
    }
}