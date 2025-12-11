package com.example.proyectointegradordam.ui.viewmodel

sealed class ProductoUIState {
    object Idle : ProductoUIState()
    object Loading : ProductoUIState()
    data class Success(val mensaje: String) : ProductoUIState()
    data class Error(val error: String) : ProductoUIState()
}
