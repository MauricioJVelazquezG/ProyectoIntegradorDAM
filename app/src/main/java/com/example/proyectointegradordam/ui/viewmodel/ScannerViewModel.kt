package com.example.proyectointegradordam.ui.Screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyectointegradordam.data.model.Producto
import com.example.proyectointegradordam.data.repository.ProductoRepository
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

// Eventos de Navegación
sealed interface ScannerNavigationEvent {
    data class GoToFound(val producto: Producto) : ScannerNavigationEvent
    data class GoToAdd(val codigo: String) : ScannerNavigationEvent
}

class ScannerViewModel(
    private val repository: ProductoRepository // Usamos tu clase concreta
) : ViewModel() {

    private val _navigationEvent = Channel<ScannerNavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    var isLoading by androidx.compose.runtime.mutableStateOf(false) // Estado simple

    private var lastScannedCode: String? = null
    private var lastScanTime: Long = 0
    private val DEBOUNCE_TIME = 2000L

    fun onBarcodeDetected(code: String) {
        val currentTime = System.currentTimeMillis()

        if (code == lastScannedCode && (currentTime - lastScanTime) < DEBOUNCE_TIME) {
            return
        }
        if (isLoading) return

        lastScannedCode = code
        lastScanTime = currentTime
        isLoading = true

        viewModelScope.launch {
            try {
                // Usamos tu método buscarPorCodigo
                val producto = repository.buscarPorCodigo(code)

                if (producto != null) {
                    _navigationEvent.send(ScannerNavigationEvent.GoToFound(producto))
                } else {
                    _navigationEvent.send(ScannerNavigationEvent.GoToAdd(code))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }
}

// Factory Ajustado: Crea tu ProductoRepository directamente
class ScannerViewModelFactory(
    private val supabaseClient: SupabaseClient // Lo dejamos por si lo ocupas, aunque tu repo usa SupabaseService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScannerViewModel::class.java)) {
            // AQUÍ ESTÁ EL CAMBIO: Instanciamos tu repo directamente
            val repo = ProductoRepository()
            @Suppress("UNCHECKED_CAST")
            return ScannerViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}