package com.example.proyectointegradordam.ui.Screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.proyectointegradordam.data.model.Producto
import com.example.proyectointegradordam.ui.utils.BarcodeAnalyzer
import io.github.jan.supabase.SupabaseClient
import java.util.concurrent.Executors

@Composable
fun ScannerScreen(
    supabaseClient: SupabaseClient,
    onProductoEncontrado: (Producto) -> Unit,
    onProductoNoEncontrado: (String) -> Unit
) {
    // Esto funciona ahora porque el Factory crea el ProductoRepository correcto
    val viewModel: ScannerViewModel = viewModel(
        factory = ScannerViewModelFactory(supabaseClient)
    )

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // --- ESCUCHAMOS EVENTOS DE NAVEGACIÓN ---
    // LaunchedEffect con Unit se ejecuta al inicio. Collect observa el flujo.
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is ScannerNavigationEvent.GoToFound -> {
                    onProductoEncontrado(event.producto)
                }
                is ScannerNavigationEvent.GoToAdd -> {
                    onProductoNoEncontrado(event.codigo)
                }
            }
        }
    }

    // --- PERMISOS ---
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasCameraPermission = granted }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // --- UI ---
    Box(modifier = Modifier.fillMaxSize()) {
        if (hasCameraPermission) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                    }

                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        val imageAnalyzer = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also {
                                it.setAnalyzer(
                                    Executors.newSingleThreadExecutor(),
                                    BarcodeAnalyzer { barcode ->
                                        // Pasamos el código al ViewModel
                                        viewModel.onBarcodeDetected(barcode)
                                    }
                                )
                            }

                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_BACK_CAMERA,
                                preview,
                                imageAnalyzer
                            )
                        } catch (exc: Exception) {
                            Log.e("ScannerScreen", "Error al vincular cámara", exc)
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            // Guía visual (Cuadro rojo)
            Box(
                modifier = Modifier
                    .size(280.dp) // Un poco más grande
                    .border(3.dp, Color.Red, RoundedCornerShape(16.dp))
                    .align(Alignment.Center)
            )

            // Indicador "Procesando..." si el ViewModel está ocupado
            if (viewModel.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Verificando producto...", color = Color.White)
                    }
                }
            }

        } else {
            // Mensaje si no hay permisos
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Se requiere permiso de cámara para escanear.")
            }
        }
    }
}