// app/src/main/java/fct/nova/beeppisca/ui/home/HomeActivity.kt
package fct.nova.beeppisca.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import fct.nova.beeppisca.domain.MomentLocation
import fct.nova.beeppisca.storage.BusRepository
import fct.nova.beeppisca.storage.LocalDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

private val Context.appDataStore by preferencesDataStore("user_prefs")

class HomeActivity : ComponentActivity() {

    private val localDataStore by lazy { LocalDataStore(appDataStore) }
    private val busRepo = BusRepository()

    // launcher for location permission
    private val permLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        if (results[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
            initLanding()
        } else {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1) Ensure location permission
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> initLanding()
            else -> permLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    @Suppress("MissingPermission")
    private fun initLanding() {
        // 2) Obtain last known location (or fallback to 0,0)
        val lm  = getSystemService(LocationManager::class.java)
        val loc = lm?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val momentLoc = loc
            ?.let { MomentLocation(it.latitude, it.longitude, it.time) }
            ?: MomentLocation(0.0, 0.0, System.currentTimeMillis())

        lifecycleScope.launch {
            // 3) For debugging/demo: choose which built-in user to “log in” as:
            //    Uncomment one of the lines below:
            // localDataStore.loginAsAdmin()
            localDataStore.loginAsBasic()

            // 4) Read current user ID from DataStore
            val userId = localDataStore.userIdFlow.firstOrNull()

            // 5) Determine whether there’s a nearby stop
            val stop = busRepo.getBusStopForLocation(
                momentLoc.latitude, momentLoc.longitude
            )

            // 6) Navigate to the appropriate screen
            if (stop == null) {
                NoStopActivity.launch(
                    ctx   = this@HomeActivity,
                    loc       = momentLoc,
                    userId    = userId
                )
            } else {
                AtStopActivity.launch(
                    ctx   = this@HomeActivity,
                    loc       = momentLoc,
                    userId    = userId
                )
            }
            // 7) Close HomeActivity so back returns out of the app
            finish()
        }
    }
}
