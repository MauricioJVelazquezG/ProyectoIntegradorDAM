package com.example.proyectointegradordam.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DecisionesViewModel : ViewModel() {

    // Estado interno mutable
    private val _uiState = MutableStateFlow<DecisionesUiState>(DecisionesUiState.Idle)

    // Estado observado por la UI
    val uiState: StateFlow<DecisionesUiState> = _uiState.asStateFlow()


    // ---------------------------
    // ACCIONES DE LA PANTALLA
    // ---------------------------

    fun seleccionarEscanear() {
        viewModelScope.launch {
            _uiState.value = DecisionesUiState.Loading

            // Simulación ligera de proceso (coherente con arquitectura async)
            delay(150)

            _uiState.value = DecisionesUiState.IrEscanear
        }
    }

    fun seleccionarInventario() {
        viewModelScope.launch {
            _uiState.value = DecisionesUiState.Loading

            delay(150)

            _uiState.value = DecisionesUiState.IrInventario
        }
    }


    // ---------------------------
    // LIMPIAR ESTADO
    // ---------------------------

    fun limpiarEstado() {
        _uiState.value = DecisionesUiState.Idle
    }
}
