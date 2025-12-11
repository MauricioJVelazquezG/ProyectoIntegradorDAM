package com.example.proyectointegradordam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectointegradordam.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistroViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState

    fun registerUser(correo: String, contra: String, contraConfirmar: String) {
        if (contra != contraConfirmar) {
            _uiState.value = AuthUiState.Error("Las contraseñas no coinciden.")
            return
        }
        if (correo.isBlank() || contra.length < 6) {
            _uiState.value = AuthUiState.Error("Verifique email y contraseña (mínimo 6 caracteres).")
            return
        }

        _uiState.value = AuthUiState.Loading

        viewModelScope.launch {
            try {
                authRepository.registro(correo, contra)
                _uiState.value = AuthUiState.RegistrationSuccess

            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error("Error: ${e.message ?: "Fallo desconocido"}")
            }
        }
    }
}