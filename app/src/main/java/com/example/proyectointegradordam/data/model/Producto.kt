package com.example.proyectointegradordam.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Producto(
    val id: Int? = null, // Supabase lo genera automático
    @SerialName("codigo_barras") val codigoBarras: String,
    val nombre: String,
    val precio: Double,
    val categoria: String = "General", // Campo nuevo
    val stock: Int = 0, // Campo nuevo (antes era "cantidad" String, mejor usar Int)
    val descripcion: String? = null
    // Nota sobre imágenes: Las Bases de Datos no guardan "R.drawable.id".
    // Por ahora usaremos una lógica en la vista para poner el icono según la categoría.
)