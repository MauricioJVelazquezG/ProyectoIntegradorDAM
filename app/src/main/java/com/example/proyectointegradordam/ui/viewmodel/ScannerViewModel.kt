package com.example.proyectointegradordam.ui.Screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyectointegradordam.data.model.Producto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Estado de la UI
data class ScannerUiState(
    val lastScannedCode: String? = null,
    val product: Producto? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class ScannerViewModel(
    private val supabase: SupabaseClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()

    // Control para no saturar con consultas si el usuario sigue apuntando al mismo código
    private var lastProcessedCode: String? = null
    private var lastProcessedTime: Long = 0

    fun onBarcodeDetected(code: String) {
        val now = System.currentTimeMillis()
        // Debounce de 2 segundos para el mismo código
        if (code == lastProcessedCode && (now - lastProcessedTime) < 2000) return

        lastProcessedCode = code
        lastProcessedTime = now
        fetchProduct(code)
    }

    private fun fetchProduct(code: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, lastScannedCode = code, error = null, product = null)

            try {
                // SINTAXIS SUPABASE 3.0.0
                // Asumiendo que tu tabla se llama "productos" y la columna "codigo_barras"
                val result = supabase.from("productos")
                    .select {
                        filter {
                            eq("codigo_barras", code)
                        }
                    }
                    .decodeSingleOrNull<Producto>()

                if (result != null) {
                    _uiState.value = _uiState.value.copy(isLoading = false, product = result)
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = "Producto no encontrado")
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Error: ${e.message}")
            }
        }
    }
}

// Factory para inyectar SupabaseClient manualmente sin Dagger/Hilt
class ScannerViewModelFactory(private val supabase: SupabaseClient) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScannerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScannerViewModel(supabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}