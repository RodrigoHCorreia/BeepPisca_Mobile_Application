package fct.nova.beeppisca.ui.home

import android.Manifest
import android.location.LocationManager
import android.os.Bundle
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import fct.nova.beeppisca.R
import fct.nova.beeppisca.domain.MomentLocation
import fct.nova.beeppisca.ui.theme.BeepPiscaTheme

class HomeActivity : ComponentActivity() {

    private val landingViewModel: LandingViewModel by viewModels()

    // Register for permission results
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        val fineGranted = results[Manifest.permission.ACCESS_FINE_LOCATION] == true
        if (fineGranted) {
            initLanding()
        } else {
            showPermissionRationale()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when {
            // Already have permission
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                initLanding()
            }
            // Denied once before
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                showPermissionRationale()
            }
            // First-time request
            else -> {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun showPermissionRationale() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.permission_location_title))
            .setMessage(getString(R.string.permission_location_message))
            .setPositiveButton(R.string.button_request_permission) { _, _ ->
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
            .setCancelable(false)
            .show()
    }

    @Suppress("MissingPermission") // permissions are already granted at this point
    private fun initLanding() {
        // Read last known location or fallback
        val lm = getSystemService(LOCATION_SERVICE) as LocationManager
        val loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val momentLoc = loc?.let {
            MomentLocation(it.latitude, it.longitude, it.time)
        } ?: MomentLocation(0.0, 0.0, System.currentTimeMillis())

        // Trigger ViewModel load
        landingViewModel.load("CURRENT_USER_ID", momentLoc)

        // Render Compose UI
        setContent {
            BeepPiscaTheme {
                LandingScreen(
                    viewModel  = landingViewModel,
                    onValidate = { /* TODO: implement validate */ },
                    onBuy      = { /* TODO: implement purchase flow */ },
                    onBack     = { finish() } // Close activity on back
                )
            }
        }
    }
}
