package com.example.proyectointegradordam.data.repository

import com.example.proyectointegradordam.data.SupabaseService
import com.example.proyectointegradordam.data.model.Producto
import io.github.jan.supabase.postgrest.from

class ProductoRepository {

    // Insertar un producto (Para tu pantalla AgregarProducto.kt)
    suspend fun agregarProducto(producto: Producto) {
        try {
            SupabaseService.client
                .from("productos") // Nombre exacto de tu tabla en Supabase
                .insert(producto)
        } catch (e: Exception) {
            throw e
        }
    }

    // Buscar por código de barras (Para escanear)
    suspend fun buscarPorCodigo(codigo: String): Producto? {
        return try {
            SupabaseService.client
                .from("productos")
                .select {
                    filter {
                        // "codigo_barras" es la columna en la BD
                        eq("codigo_barras", codigo)
                    }
                }
                .decodeSingleOrNull<Producto>()
        } catch (e: Exception) {
            null
        }
    }

    // Obtener todos los productos (Para inventario)
    suspend fun obtenerTodos(): List<Producto> {
        return SupabaseService.client
            .from("productos")
            .select()
            .decodeList<Producto>()
    }


}