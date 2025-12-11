package com.example.proyectointegradordam.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectointegradordam.data.model.Producto
import com.example.proyectointegradordam.ui.Screens.*
import io.github.jan.supabase.SupabaseClient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// 1. Definición de Rutas
sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Decision : Screen("decision")
    data object Menu : Screen("menu_inventario")
    data object Scanner : Screen("scanner")

    data object ProductoEncontrado : Screen("producto_encontrado/{productoJson}") {
        fun createRoute(producto: Producto): String {
            val json = Json.encodeToString(producto)
            val jsonEncoded = Uri.encode(json)
            return "producto_encontrado/$jsonEncoded"
        }
    }

    data object AgregarProducto : Screen("agregar_producto/{productoJson}") {
        fun createRoute(codigoBarras: String): String {
            // Producto temporal solo para transportar el código
            val tempProd = Producto(codigoBarras = codigoBarras, nombre = "", precio = 0.0)
            val json = Json.encodeToString(tempProd)
            val jsonEncoded = Uri.encode(json)
            return "agregar_producto/$jsonEncoded"
        }
    }
}

@Composable
fun AppNavigation(
    supabaseClient: SupabaseClient
) {
    val navController = rememberNavController()

    // --- CORRECCIÓN AQUÍ ---
    // NO instanciamos 'ProductoRepositoryImpl' porque ya no existe o da error.
    // Las pantallas crearán su propio repositorio internamente o usando el Factory.

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {

        // --- 1. LOGIN ---
        composable(Screen.Login.route) {
            LoginScreen(
                supabaseClient = supabaseClient,
                onLoginSuccess = {
                    navController.navigate(Screen.Decision.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // --- REGISTRO ---
        composable(Screen.Register.route) {
            // RegisterScreen(...) // Pendiente
        }

        // --- 2. DECISIÓN ---
        composable(Screen.Decision.route) {
            Decisiones(
                onIrEscanear = { navController.navigate(Screen.Scanner.route) },
                onIrInventario = { navController.navigate(Screen.Menu.route) }
            )
        }

        // --- 3. MENU (INVENTARIO) ---
        composable(Screen.Menu.route) {
            Menu(
                supabaseClient = supabaseClient,
                onBack = { navController.popBackStack() }
            )
        }

        // --- 4. SCANNER ---
        composable(Screen.Scanner.route) {
            ScannerScreen(
                supabaseClient = supabaseClient,
                onProductoEncontrado = { producto ->
                    val ruta = Screen.ProductoEncontrado.createRoute(producto)
                    navController.navigate(ruta)
                },
                onProductoNoEncontrado = { codigo ->
                    val ruta = Screen.AgregarProducto.createRoute(codigo)
                    navController.navigate(ruta)
                }
            )
        }

        // --- 5. PRODUCTO ENCONTRADO (Editar Stock) ---
        composable(
            route = Screen.ProductoEncontrado.route,
            arguments = listOf(navArgument("productoJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val jsonString = backStackEntry.arguments?.getString("productoJson")
            val producto = jsonString?.let { Json.decodeFromString<Producto>(it) }

            if (producto != null) {
                ProductoEncontradoScreen(
                    producto = producto,
                    onBackToScan = {
                        navController.popBackStack(Screen.Decision.route, inclusive = false)
                    }
                )
            }
        }

        // --- 6. AGREGAR PRODUCTO (Nuevo) ---
        composable(
            route = Screen.AgregarProducto.route,
            arguments = listOf(navArgument("productoJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val jsonString = backStackEntry.arguments?.getString("productoJson")
            val productoTemp = jsonString?.let { Json.decodeFromString<Producto>(it) }
            val codigoBarras = productoTemp?.codigoBarras ?: ""

            AgregarProductoScreen(
                supabaseClient = supabaseClient,
                codigoBarrasInicial = codigoBarras,
                onGuardarExitoso = {
                    navController.popBackStack(Screen.Decision.route, inclusive = false)
                },
                onCancelar = {
                    navController.popBackStack()
                }
            )
        }
    }
}