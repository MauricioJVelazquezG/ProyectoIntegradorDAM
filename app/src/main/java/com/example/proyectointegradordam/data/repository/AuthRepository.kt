package com.example.proyectointegradordam.data.repository

import com.example.proyectointegradordam.data.SupabaseService
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

class AuthRepository {

    suspend fun login(email: String, contra: String) {
        // Llama a Supabase para iniciar sesión con Email y Password
        SupabaseService.client.auth.signInWith(Email) {
            this.email = email
            this.password = contra
        }
    }

    suspend fun registro(email: String, contra: String) {
        // Llama a Supabase para registrar un nuevo usuario
        SupabaseService.client.auth.signUpWith(Email) {
            this.email = email
            this.password = contra
        }
    }

    // Función para saber si ya hay alguien logueado (útil para el Splash Screen)
    fun estaLogueado(): Boolean {
        return SupabaseService.client.auth.currentSessionOrNull() != null
    }
}