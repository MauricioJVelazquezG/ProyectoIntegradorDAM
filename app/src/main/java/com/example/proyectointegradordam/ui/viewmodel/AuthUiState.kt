package com.example.proyectointegradordam.ui.viewmodel

sealed interface AuthUiState {
    data object Idle : AuthUiState             // Esperando
    data object Loading : AuthUiState          // Cargando
    data object Success : AuthUiState          // Login exitoso
    data object RegistrationSuccess : AuthUiState //Registro exitoso
    data class Error(val mensaje: String) : AuthUiState // Error (contraseña incorrecta, etc)
}