package com.example.proyectointegradordam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.Navigation
import com.example.proyectointegradordam.ui.theme.ProyectoIntegradorDAMTheme
import com.example.proyectointegradordam.ui.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProyectoIntegradorDAMTheme {
                // Aquí llamamos al sistema de navegación
                AppNavigation()
            }
        }
    }
}
