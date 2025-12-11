package com.example.proyectointegradordam.ui.viewmodel

import com.example.proyectointegradordam.data.model.Producto

// Define los estados posibles de la pantalla
sealed interface ProductoUiState {
    data object Idle : ProductoUiState             // Estado inicial (sin hacer nada)
    data object Loading : ProductoUiState          // Cargando (Spinner)
    data class Success(val mensaje: String) : ProductoUiState // Éxito al guardar
    data class Error(val error: String) : ProductoUiState     // Error
    data class ProductoEncontrado(val producto: Producto) : ProductoUiState // Para el escáner
    data class SuccessLista(val productos: List<Producto>) : ProductoUiState
}