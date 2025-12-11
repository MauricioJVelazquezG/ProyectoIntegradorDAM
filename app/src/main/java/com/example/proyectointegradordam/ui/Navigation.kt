package com.example.proyectointegradordam.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.proyectointegradordam.data.model.Producto
import com.example.proyectointegradordam.ui.Screens.AgregarProducto
import com.example.proyectointegradordam.ui.Screens.LoginScreen
// import com.example.proyectointegradordam.ui.Screens.HomeScreen // (Asumo que crearás esta pantalla)
// import com.example.proyectointegradordam.ui.Screens.RegisterScreen // (Asumo que crearás esta)
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// 1. Definimos las rutas de forma segura (Sealed Classes)
sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")

    // Ruta con argumento: Recibe un Producto en formato JSON
    data object AgregarProducto : Screen("agregar_producto/{productoJson}") {
        // Función helper para construir la ruta con los datos
        fun createRoute(producto: Producto): String {
            val json = Json.encodeToString(producto)
            val jsonEncoded = Uri.encode(json) // Codificar para que sea seguro en la URL
            return "agregar_producto/$jsonEncoded"
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route // La pantalla inicial
    ) {

        // --- PANTALLA DE LOGIN ---
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    // Al loguearse, vamos al Home y borramos el Login del historial
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        // --- PANTALLA DE REGISTRO (Placeholder) ---
        composable(Screen.Register.route) {
            // RegisterScreen(...) // Aquí iría tu pantalla de registro
            // Por ahora un texto temporal para que no de error
            androidx.compose.material3.Text("Pantalla de Registro (Pendiente)")
        }

        // --- PANTALLA PRINCIPAL (HOME / SCANNER) ---
        composable(Screen.Home.route) {
            // Aquí deberías llamar a tu pantalla Home
            // Simulamos que al escanear, navegamos a agregar producto

            /* EJEMPLO DE USO EN TU HOME:
            HomeScreen(
                onProductoEscaneado = { productoDetectado ->
                    val ruta = Screen.AgregarProducto.createRoute(productoDetectado)
                    navController.navigate(ruta)
                }
            )
            */
            androidx.compose.material3.Text("Pantalla Home (Pendiente)")
        }

        // --- PANTALLA AGREGAR PRODUCTO ---
        composable(
            route = Screen.AgregarProducto.route,
            arguments = listOf(
                navArgument("productoJson") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            // Recuperamos el JSON y lo convertimos de vuelta a Objeto Producto
            val jsonString = backStackEntry.arguments?.getString("productoJson")

            // Convertimos el JSON a objeto Producto
            val producto = jsonString?.let {
                Json.decodeFromString<Producto>(it)
            } ?: Producto(codigoBarras = "", nombre = "", precio = 0.0) // Fallback por seguridad

            AgregarProducto(
                productoEscaneado = producto,
                onNavigateBack = {
                    navController.popBackStack() // Volver atrás
                }
            )
        }
    }
}