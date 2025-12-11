package com.example.proyectointegradordam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 1. Definimos los estados que tu UI espera
sealed interface AuthUiState {
    data object Idle : AuthUiState    // Estado inicial
    data object Loading : AuthUiState // Cargando
    data object Success : AuthUiState // Éxito
    data class Error(val mensaje: String) : AuthUiState // Error
}

// 2. El ViewModel con la lógica de Supabase
class LoginViewModel(
    private val supabase: SupabaseClient
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun iniciarSesion(correo: String, contra: String) {
        // Validaciones básicas
        if (correo.isBlank() || contra.isBlank()) {
            _uiState.value = AuthUiState.Error("Por favor llena todos los campos")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                // --- LOGIN CON SUPABASE ---
                supabase.auth.signInWith(Email) {
                    this.email = correo
                    this.password = contra
                }
                // Si pasa esta línea, fue exitoso
                _uiState.value = AuthUiState.Success
            } catch (e: Exception) {
                // Manejo de errores
                val msg = if (e.message?.contains("Invalid login credentials") == true) {
                    "Correo o contraseña incorrectos"
                } else {
                    "Error de conexión: ${e.message}"
                }
                _uiState.value = AuthUiState.Error(msg)
            }
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}

// 3. Factory para inyectar Supabase
class LoginViewModelFactory(private val supabase: SupabaseClient) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(supabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}