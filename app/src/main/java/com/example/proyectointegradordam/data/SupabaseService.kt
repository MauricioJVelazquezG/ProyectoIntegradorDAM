package com.example.proyectointegradordam.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.auth.Auth

object SupabaseService {
    // Tu URL proporcionada
    private const val SUPABASE_URL = "https://vrcwzsmtonrhennimrkg.supabase.co"
    private const val SUPABASE_KEY = "sb_publishable_8SGVxHy3jdLkJ1X7vmkP2Q_KFO_ZSiB"

    val client = createSupabaseClient(supabaseUrl = SUPABASE_URL, supabaseKey = SUPABASE_KEY) {
        install(Postgrest) // Para base de datos
        install(Auth)      // Para usuarios/login
    }
}