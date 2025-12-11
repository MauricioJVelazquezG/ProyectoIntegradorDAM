package com.example.proyectointegradordam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.proyectointegradordam.data.SupabaseService
import com.example.proyectointegradordam.ui.AppNavigation
import com.example.proyectointegradordam.ui.theme.ProyectoIntegradorDAMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilita el diseño de borde a borde (barra de estado transparente)
        enableEdgeToEdge()

        setContent {
            ProyectoIntegradorDAMTheme {
                // CORRECCIÓN: Pasamos el cliente desde tu Singleton SupabaseService
                // hacia el sistema de navegación.
                AppNavigation(supabaseClient = SupabaseService.client)
            }
        }
    }
}