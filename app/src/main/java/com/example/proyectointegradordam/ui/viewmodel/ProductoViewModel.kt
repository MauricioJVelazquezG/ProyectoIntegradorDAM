package com.example.proyectointegradordam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyectointegradordam.data.model.Producto
import com.example.proyectointegradordam.data.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ----------------------------------------------------------------
// 1. ESTADOS DE LA UI (Unificados para Lista y Agregar)
// ----------------------------------------------------------------
sealed interface ProductoUiState {
    // Estado inicial (quieto)
    data object Idle : ProductoUiState

    // Cargando (spinner)
    data object Loading : ProductoUiState

    // ÉXITO TIPO A: Se obtuvo la lista (Para el Menú)
    data class SuccessLista(val productos: List<Producto>) : ProductoUiState

    // ÉXITO TIPO B: Se guardó/agregó un producto (Para AgregarProducto)
    data class Success(val mensaje: String) : ProductoUiState

    // Error genérico
    data class Error(val error: String) : ProductoUiState
}

// ----------------------------------------------------------------
// 2. EL VIEWMODEL
// ----------------------------------------------------------------
class ProductoViewModel(
    private val repository: ProductoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProductoUiState>(ProductoUiState.Idle)
    val uiState: StateFlow<ProductoUiState> = _uiState.asStateFlow()

    /**
     * FUNCIÓN 1: Para la pantalla MENU (Cargar Inventario)
     */
    fun cargarInventario() {
        viewModelScope.launch {
            _uiState.value = ProductoUiState.Loading
            try {
                val lista = repository.obtenerTodos()
                _uiState.value = ProductoUiState.SuccessLista(lista)
            } catch (e: Exception) {
                _uiState.value = ProductoUiState.Error("Error al cargar: ${e.message}")
            }
        }
    }

    /**
     * FUNCIÓN 2: Para la pantalla AGREGAR PRODUCTO
     * Recibe Strings de los TextFields y los convierte a números.
     */
    fun agregarProducto(
        nombre: String,
        codigo: String,
        precioString: String,
        categoria: String,
        stockString: String
    ) {
        // 1. Validaciones básicas
        if (nombre.isBlank() || precioString.isBlank() || categoria.isBlank()) {
            _uiState.value = ProductoUiState.Error("Por favor completa los campos obligatorios")
            return
        }

        viewModelScope.launch {
            _uiState.value = ProductoUiState.Loading
            try {
                // 2. Convertir Strings a Números (Manejo de errores simple)
                val precio = precioString.toDoubleOrNull() ?: 0.0
                val stock = stockString.toIntOrNull() ?: 0

                // 3. Crear el objeto Producto
                val nuevoProducto = Producto(
                    codigoBarras = codigo,
                    nombre = nombre,
                    precio = precio,
                    categoria = categoria,
                    stock = stock
                )

                // 4. Llamar al repositorio para guardar en Supabase
                repository.agregarProducto(nuevoProducto)

                // 5. Notificar éxito
                _uiState.value = ProductoUiState.Success("Producto agregado correctamente")

            } catch (e: Exception) {
                _uiState.value = ProductoUiState.Error("Error al guardar: ${e.message}")
            }
        }
    }

    // Limpia el estado para evitar que Toast o Navegación se repitan al girar pantalla
    fun limpiarEstado() {
        _uiState.value = ProductoUiState.Idle
    }
}

// ----------------------------------------------------------------
// 3. FACTORY (Para inyectar el Repository)
// ----------------------------------------------------------------
class ProductoViewModelFactory(
    private val repository: ProductoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}