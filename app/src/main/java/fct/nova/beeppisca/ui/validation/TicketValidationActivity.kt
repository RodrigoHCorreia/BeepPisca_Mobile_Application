package fct.nova.beeppisca.ui.validation

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import fct.nova.beeppisca.ui.theme.BeepPiscaTheme

class TicketValidationActivity : ComponentActivity() {
    private val viewModel: TicketValidationViewModel by viewModels()

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) startScanning() else finish()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ask camera permission
        requestPermission.launch(Manifest.permission.CAMERA)

        setContent {
            BeepPiscaTheme {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    ValidationScreen(
                        viewModel = viewModel,
                        userId = intent.getStringExtra("EXTRA_USER_ID")!!,
                        onFinish = { finish() }
                    )
                }
            }
        }
    }

    private fun startScanning() {
        // no-op: actual binding is done in the composable’s AndroidView
    }
}
