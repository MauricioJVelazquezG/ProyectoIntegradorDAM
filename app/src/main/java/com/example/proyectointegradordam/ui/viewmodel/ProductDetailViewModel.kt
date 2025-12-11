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

// 1. Estado de la UI
data class ProductDetailUiState(
    val currentStock: Int = 0,
    val isSaving: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

// 2. El ViewModel
class ProductDetailViewModel(
    private val repository: ProductoRepository,
    private val productoInicial: Producto
) : ViewModel() {

    // Inicializamos el estado con el stock que trae el producto escaneado
    private val _uiState = MutableStateFlow(ProductDetailUiState(currentStock = productoInicial.stock))
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    // Sumar 1 al stock local
    fun incrementStock() {
        val nuevaCantidad = _uiState.value.currentStock + 1
        _uiState.value = _uiState.value.copy(currentStock = nuevaCantidad)
    }

    // Restar 1 al stock local (validando que no sea negativo)
    fun decrementStock() {
        val cantidadActual = _uiState.value.currentStock
        if (cantidadActual > 0) {
            _uiState.value = _uiState.value.copy(currentStock = cantidadActual - 1)
        }
    }

    // Guardar cambios en Supabase
    fun saveChanges() {
        val prodId = productoInicial.id

        // Validación de seguridad por si el ID viene nulo
        if (prodId == null) {
            _uiState.value = _uiState.value.copy(error = "Error: El producto no tiene un ID válido.")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true, error = null)

            try {
                // Llamamos a tu repositorio (que ya incluye el try-catch de Supabase)
                repository.actualizarStock(prodId, _uiState.value.currentStock)

                // Si no lanza excepción, fue exitoso
                _uiState.value = _uiState.value.copy(isSaving = false, isSuccess = true)

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    error = "Error al actualizar: ${e.message}"
                )
            }
        }
    }

    // Resetea el flag de éxito para que no se dispare la navegación múltiples veces
    fun resetSuccessState() {
        _uiState.value = _uiState.value.copy(isSuccess = false)
    }
}

// 3. Factory para inyectar dependencias (Repo y Producto)
class ProductDetailViewModelFactory(
    private val repository: ProductoRepository,
    private val producto: Producto
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductDetailViewModel(repository, producto) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}