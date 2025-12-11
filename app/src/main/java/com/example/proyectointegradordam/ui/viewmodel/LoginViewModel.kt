package com.example.proyectointegradordam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectointegradordam.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun iniciarSesion(email: String, contra: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                if (email.isBlank() || contra.isBlank()) {
                    throw Exception("Por favor llena todos los campos")
                }

                repository.login(email, contra)
                _uiState.value = AuthUiState.Success

            } catch (e: Exception) {
                // Supabase suele devolver errores en inglés, aquí podrías traducirlos si quisieras
                _uiState.value = AuthUiState.Error(e.message ?: "Error al iniciar sesión")
            }
        }
    }

    // Opcional: Función para registrarse si decides usarla en la misma pantalla o navegar
    fun registrarse(email: String, contra: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                repository.registro(email, contra)
                _uiState.value = AuthUiState.Success
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: "Error al registrarse")
            }
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}