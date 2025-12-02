package com.example.hanashinomori.view

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

@Composable
fun CameraPreview(
    onQrScanned: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Usar rememberUpdatedState para el callback
    val currentOnQrScanned by rememberUpdatedState(onQrScanned)

    var isScanning by remember { mutableStateOf(true) }
    var hasScanned by remember { mutableStateOf(false) }
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }

    // Resetear el estado cuando se monta el composable
    LaunchedEffect(Unit) {
        isScanning = true
        hasScanned = false
        Log.d("CameraPreview", "🎥 Camera Preview iniciado - Reseteo completo")
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            Log.d("CameraPreview", "🏗️ Creando PreviewView")

            val previewView = PreviewView(ctx).apply {
                scaleType = PreviewView.ScaleType.FILL_START
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }

            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                try {
                    val provider = cameraProviderFuture.get()
                    cameraProvider = provider

                    Log.d("CameraPreview", "📷 Provider obtenido, unbinding...")
                    provider.unbindAll()

                    val preview = androidx.camera.core.Preview.Builder()
                        .setTargetResolution(android.util.Size(1280, 720))
                        .build()

                    Log.d("CameraPreview", "🔗 Conectando surface provider...")
                    preview.setSurfaceProvider(previewView.surfaceProvider)

                    val imageAnalyzer = ImageAnalysis.Builder()
                        .setTargetResolution(android.util.Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    val scanner = BarcodeScanning.getClient()

                    imageAnalyzer.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imageProxy ->
                        if (!isScanning || hasScanned) {
                            imageProxy.close()
                            return@setAnalyzer
                        }

                        processImageProxy(imageProxy, scanner) { valorQr ->
                            if (!hasScanned) {
                                hasScanned = true
                                isScanning = false
                                Log.d("CameraPreview", "✅ QR Escaneado: $valorQr")
                                currentOnQrScanned(valorQr)
                            }
                        }
                    }

                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    Log.d("CameraPreview", "🎬 Binding use cases...")
                    val camera = provider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalyzer
                    )

                    Log.d("CameraPreview", "✅ Cámara vinculada: ${camera.cameraInfo.cameraState.value}")

                } catch (e: Exception) {
                    Log.e("CameraPreview", "❌ Error al iniciar cámara: ${e.message}", e)
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        }
    )

    DisposableEffect(Unit) {
        onDispose {
            Log.d("CameraPreview", "🛑 Limpiando cámara")
            cameraProvider?.unbindAll()
        }
    }
}



@OptIn(ExperimentalGetImage::class)
private fun processImageProxy(
    imageProxy: ImageProxy,
    scanner: BarcodeScanner,
    onQrFound: (String) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage == null) {
        imageProxy.close()
        return
    }

    try {
        val image = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                if (barcodes.isNotEmpty()) {
                    Log.d("CameraPreview", "🔎 Códigos encontrados: ${barcodes.size}")
                    for (barcode in barcodes) {
                        Log.d("CameraPreview", "📊 Tipo: ${barcode.format}, Valor: ${barcode.rawValue}")
                    }
                }

                for (barcode in barcodes) {
                    val rawValue = barcode.rawValue
                    if (!rawValue.isNullOrBlank()) {
                        // Aceptar CUALQUIER código (QR, barras, etc)
                        Log.d("CameraPreview", "✅ CÓDIGO DETECTADO: Format=${barcode.format}, Value=$rawValue")
                        onQrFound(rawValue)
                        break
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("CameraPreview", "❌ Error ML Kit: ${e.message}")
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    } catch (e: Exception) {
        Log.e("CameraPreview", "❌ Error procesando: ${e.message}")
        imageProxy.close()
    }
}

