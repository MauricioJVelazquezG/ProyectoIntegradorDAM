package com.example.proyectointegradordam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectointegradordam.data.model.Producto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductoViewModel(
    private val supabase: SupabaseClient // <--- se inyecta el cliente Supabase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductoUiState>(ProductoUiState.Idle)
    val uiState: StateFlow<ProductoUiState> = _uiState.asStateFlow()

    fun agregarProducto(
        nombre: String,
        codigo: String,
        precioString: String,
        categoria: String? = null,
        stock: String? = null,
        imageUrl: String? = null
    ) {
        if (nombre.isBlank() || precioString.isBlank()) {
            _uiState.value = ProductoUiState.Error("Todos los campos son obligatorios")
            return
        }

        val precio = precioString.toFloatOrNull()
        val stockInt = stock?.toIntOrNull() ?: 0

        if (precio == null) {
            _uiState.value = ProductoUiState.Error("El precio debe ser numérico")
            return
        }

        _uiState.value = ProductoUiState.Loading

        viewModelScope.launch {
            try {
                // Mapeamos al formato esperado por Supabase
                val data = mapOf(
                    "name" to nombre,
                    "description" to (categoria ?: "Sin categoría"),
                    "price" to precio,
                    "stock" to stockInt,
                    "image_url" to (imageUrl ?: ""),
                    "codigo_barras" to codigo // si lo agregaste en BD
                )

                supabase
                    .from("productos")
                    .insert(data)

                _uiState.value = ProductoUiState.Success("Producto agregado correctamente")

            } catch (e: Exception) {
                _uiState.value = ProductoUiState.Error("Error: ${e.message}")
            }
        }
    }

    fun limpiarEstado() {
        _uiState.value = ProductoUiState.Idle
    }
}
