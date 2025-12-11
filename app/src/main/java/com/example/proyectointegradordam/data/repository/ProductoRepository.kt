package com.example.proyectointegradordam.data.repository

import android.util.Log
import com.example.proyectointegradordam.data.SupabaseService
import com.example.proyectointegradordam.data.model.Producto
import io.github.jan.supabase.postgrest.from

class ProductoRepository {

    // Insertar un producto
    suspend fun agregarProducto(producto: Producto) {
        try {
            SupabaseService.client
                .from("productos")
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
                .from("productos")
                .select {
                    filter {
                        eq("codigo_barras", codigo)
                    }
                }
                .decodeSingleOrNull<Producto>()
        } catch (e: Exception) {
            Log.e("Repo", "Error al buscar: ${e.message}")
            null
        }
    }

    // Obtener todos los productos (Con Try-Catch para seguridad)
    suspend fun obtenerTodos(): List<Producto> {
        return try {
            SupabaseService.client
                .from("productos")
                .select()
                .decodeList<Producto>()
        } catch (e: Exception) {
            Log.e("Repo", "Error al obtener todos: ${e.message}")
            emptyList() // Regresa lista vacía en vez de crashear
        }
    }

    // --- NUEVA FUNCIÓN NECESARIA PARA TU PANTALLA ---
    suspend fun actualizarStock(id: Int, nuevoStock: Int) {
        try {
            SupabaseService.client
                .from("productos")
                .update(
                    {
                        set("stock", nuevoStock)
                    }
                ) {
                    filter {
                        eq("id", id)
                    }
                }
        } catch (e: Exception) {
            Log.e("Repo", "Error al actualizar stock: ${e.message}")
            throw e
        }
    }
}