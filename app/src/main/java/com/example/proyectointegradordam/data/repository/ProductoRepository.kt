package com.example.proyectointegradordam.data.repository

import android.util.Log
import com.example.proyectointegradordam.data.SupabaseService
import com.example.proyectointegradordam.data.model.Producto
import io.github.jan.supabase.postgrest.from

class ProductoRepository {

    // Insertar un producto
    // Nota: Aquí usa los @SerialName de tu modelo Producto.kt, así que si ya pusiste @SerialName("barcode"), esto funcionará bien.
    suspend fun agregarProducto(producto: Producto) {
        try {
            SupabaseService.client
                .from("products")
                .insert(producto)
        } catch (e: Exception) {
            Log.e("Repo", "Error al agregar: ${e.message}")
            throw e
        }
    }

    // Buscar por código de barras
    suspend fun buscarPorCodigo(codigo: String): Producto? {
        return try {
            SupabaseService.client
                .from("products")
                .select {
                    filter {
                        // CORRECCIÓN: Usar "barcode" (nombre en BD) en vez de "codigo_barras"
                        eq("barcode", codigo)
                    }
                }
                .decodeSingleOrNull<Producto>()
        } catch (e: Exception) {
            Log.e("Repo", "Error al buscar: ${e.message}")
            null
        }
    }

    // Obtener todos los productos
    suspend fun obtenerTodos(): List<Producto> {
        return try {
            SupabaseService.client
                .from("products")
                .select()
                .decodeList<Producto>()
        } catch (e: Exception) {
            Log.e("Repo", "Error al obtener todos: ${e.message}")
            emptyList()
        }
    }

    // Actualizar Stock
    // CORRECCIÓN: El ID ahora es String (UUID)
    suspend fun actualizarStock(id: String, nuevoStock: Int) {
        try {
            SupabaseService.client
                .from("products")
                .update(
                    {
                        // Asegúrate que la columna en BD sea "stock"
                        set("stock", nuevoStock)
                    }
                ) {
                    filter {
                        // Asegúrate que la columna ID en BD sea "id"
                        eq("id", id)
                    }
                }
        } catch (e: Exception) {
            Log.e("Repo", "Error al actualizar stock: ${e.message}")
            throw e
        }
    }
}