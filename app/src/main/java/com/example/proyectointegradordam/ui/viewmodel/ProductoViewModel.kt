package com.example.proyectointegradordam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectointegradordam.data.model.Producto
import com.example.proyectointegradordam.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductoViewModel(
    // Inyectamos el repo (por defecto crea uno nuevo para facilitar la instancia)
    private val repository: ProductoRepository = ProductoRepository()
) : ViewModel() {

    // Estado mutable privado
    private val _uiState = MutableStateFlow<ProductoUiState>(ProductoUiState.Idle)
    // Estado público de solo lectura para la UI
    val uiState: StateFlow<ProductoUiState> = _uiState.asStateFlow()

    // Función para agregar producto
    fun agregarProducto(nombre: String, codigo: String, precioString: String) {
        viewModelScope.launch {
            _uiState.value = ProductoUiState.Loading

            try {
                // Validación básica
                if (nombre.isBlank() || codigo.isBlank() || precioString.isBlank()) {
                    throw Exception("Todos los campos son obligatorios")
                }

                val precio = precioString.toDoubleOrNull() ?: throw Exception("El precio no es válido")

                val nuevoProducto = Producto(
                    codigoBarras = codigo,
                    nombre = nombre,
                    precio = precio
                )

                repository.agregarProducto(nuevoProducto)
                _uiState.value = ProductoUiState.Success("Producto guardado correctamente")

            } catch (e: Exception) {
                _uiState.value = ProductoUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    // Función para buscar por código de barras
    fun buscarProducto(codigo: String) {
        viewModelScope.launch {
            _uiState.value = ProductoUiState.Loading
            try {
                val producto = repository.buscarPorCodigo(codigo)
                if (producto != null) {
                    _uiState.value = ProductoUiState.ProductoEncontrado(producto)
                } else {
                    _uiState.value = ProductoUiState.Error("Producto no encontrado")
                }
            } catch (e: Exception) {
                _uiState.value = ProductoUiState.Error("Error de conexión")
            }
        }
    }

    // Función para limpiar estado (útil al navegar o cerrar diálogos)
    fun limpiarEstado() {
        _uiState.value = ProductoUiState.Idle
    }

    fun cargarInventario() {
        viewModelScope.launch {
            _uiState.value = ProductoUiState.Loading
            try {
                val lista = repository.obtenerTodos()
                _uiState.value = ProductoUiState.SuccessLista(lista)
            } catch (e: Exception) {
                _uiState.value = ProductoUiState.Error("Error al cargar inventario: ${e.message}")
            }
        }
    }
}