package com.example.proyectointegradordam.ui.components.texts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TextField(
    value: MutableState<String>,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value.value,
        onValueChange = { value.value = it },
        modifier = modifier.fillMaxWidth(),
        label = { Text(label, color = Color(0xFF3C2E2A)) },
        shape = RoundedCornerShape(15.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color(0xFFEDEDED),
            unfocusedIndicatorColor = Color(0xFFEDEDED),
            cursorColor = Color(0xFF6B4A3A)
        )
    )
}