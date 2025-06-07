package fct.nova.beeppisca.ui.validation

import android.annotation.SuppressLint
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview as CameraXPreview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import fct.nova.beeppisca.R
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ValidationScreen(
    viewModel: TicketValidationViewModel,
    userId: String,
    onFinish: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Box(Modifier.fillMaxSize()) {
        when (state) {
            ValidationState.Scanning -> CameraPreview { qr ->
                viewModel.onQrDetected(qr, userId)
            }
            ValidationState.Validating -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            ValidationState.Success -> {
                // icon + text centered
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.Green,
                        modifier = Modifier.size(128.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.validation_successful),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                // OK button at bottom
                Button(
                    onClick = onFinish,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(24.dp)
                        .width(160.dp)
                        .height(48.dp)
                ) {
                    Text(stringResource(R.string.ok))
                }
            }
            ValidationState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(128.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.validation_failed),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Button(
                    onClick = { viewModel.reset() },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(24.dp)
                        .width(160.dp)
                        .height(48.dp)
                ) {
                    Text(stringResource(R.string.try_again))
                }
            }
        }
    }
}

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun CameraPreview(onQrFound: (String) -> Unit) {
    val context = LocalContext.current
    val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
        ProcessCameraProvider.getInstance(context)

    // Create ML Kit scanner
    val scanner = BarcodeScanning.getClient()

    AndroidView(factory = { ctx ->
        val previewView = PreviewView(ctx)
        cameraProviderFuture.addListener({
            val provider = cameraProviderFuture.get()

            // Preview use case
            val previewUseCase = CameraXPreview.Builder()
                .build()
                .also { it.setSurfaceProvider(previewView.surfaceProvider) }

            // Analysis use case
            val analysisUseCase = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { analysis ->
                    analysis.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imageProxy ->
                        val mediaImage = imageProxy.image
                        if (mediaImage != null) {
                            val input = InputImage.fromMediaImage(
                                mediaImage, imageProxy.imageInfo.rotationDegrees
                            )
                            scanner.process(input)
                                .addOnSuccessListener { barcodes ->
                                    for (barcode in barcodes) {
                                        barcode.rawValue?.let { value ->
                                            onQrFound(value)
                                            // stop analyzing until user resets
                                            analysis.clearAnalyzer()
                                        }
                                    }
                                }
                                .addOnCompleteListener { imageProxy.close() }
                        } else {
                            imageProxy.close()
                        }
                    }
                }

            provider.unbindAll()
            provider.bindToLifecycle(
                ctx as androidx.lifecycle.LifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                previewUseCase,
                analysisUseCase
            )
        }, ContextCompat.getMainExecutor(ctx))
        previewView
    }, modifier = Modifier.fillMaxSize())
}


// === Previews ===

@Preview(showBackground = true, name = "Scanning")
@Composable fun PreviewScanning() {
    val vm = object : TicketValidationViewModel() {
        override val state = MutableStateFlow(ValidationState.Scanning)
    }
    ValidationScreen(vm, userId = "u1", onFinish = {})
}

@Preview(showBackground = true, name = "Validating")
@Composable fun PreviewValidating() {
    val vm = object : TicketValidationViewModel() {
        override val state = MutableStateFlow(ValidationState.Validating)
    }
    ValidationScreen(vm, "u1") {}
}

@Preview(showBackground = true, name = "Success")
@Composable fun PreviewSuccess() {
    val vm = object : TicketValidationViewModel() {
        override val state = MutableStateFlow(ValidationState.Success)
    }
    ValidationScreen(vm, "u1") {}
}

@Preview(showBackground = true, name = "Error")
@Composable fun PreviewError() {
    val vm = object : TicketValidationViewModel() {
        override val state = MutableStateFlow(ValidationState.Error)
    }
    ValidationScreen(vm, "u1") {}
}
