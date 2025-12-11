package com.example.proyectointegradordam.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Producto(
    // CORRECCIÓN 1: Cambiamos Int por String porque recibimos un UUID
    val id: String? = null,

    // CORRECCIÓN 2: Mapeamos los nombres en inglés (JSON) a tus variables en español
    @SerialName("barcode")
    val codigoBarras: String,

    @SerialName("name")
    val nombre: String,

    @SerialName("price")
    val precio: Double,

    @SerialName("stock")
    val stock: Int,

    @SerialName("category")
    val categoria: String = "General",

    @SerialName("description")
    val descripcion: String? = null
)