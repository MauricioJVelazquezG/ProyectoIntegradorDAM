package com.example.proyectointegradordam.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// 1. Definimos los estados posibles de la pantalla
sealed interface DecisionesUiState {
    data object Idle : DecisionesUiState          // Estado quieto (esperando clic)
    data object IrEscanear : DecisionesUiState    // El usuario quiere ir a escanear
    data object IrInventario : DecisionesUiState  // El usuario quiere ir al inventario
}

// 2. La clase ViewModel
class DecisionesViewModel : ViewModel() {

    // Estado interno mutable
    private val _uiState = MutableStateFlow<DecisionesUiState>(DecisionesUiState.Idle)
    // Estado público inmutable (para que la UI lo lea)
    val uiState: StateFlow<DecisionesUiState> = _uiState.asStateFlow()

    // Función cuando clickean "Escanear"
    fun seleccionarEscanear() {
        _uiState.value = DecisionesUiState.IrEscanear
    }

    // Función cuando clickean "Inventario"
    fun seleccionarInventario() {
        _uiState.value = DecisionesUiState.IrInventario
    }

    // Función MUY IMPORTANTE: Resetea el estado a Idle después de navegar
    // Si no hacemos esto, al volver atrás, la app intentará navegar de nuevo automáticamente
    fun limpiarEstado() {
        _uiState.value = DecisionesUiState.Idle
    }
}