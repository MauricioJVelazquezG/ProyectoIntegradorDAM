package com.example.proyectointegradordam.ui.viewmodel


sealed class DecisionesUiState {
    object Idle : DecisionesUiState()
    object Loading : DecisionesUiState()

    object IrEscanear : DecisionesUiState()
    object IrInventario : DecisionesUiState()

    data class Error(val mensaje: String) : DecisionesUiState()
}
