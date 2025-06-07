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
import fct.nova.beeppisca.storage.LocalDataStore
import fct.nova.beeppisca.storage.BusRepository
import fct.nova.beeppisca.ui.adminView.AdminViewActivity
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

private val Context.appDataStore by preferencesDataStore("user_prefs")

class HomeActivity : ComponentActivity() {

    private val localDataStore by lazy { LocalDataStore(appDataStore) }
    private val busRepo by lazy { BusRepository() }

    private val permLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        if (perms[Manifest.permission.ACCESS_FINE_LOCATION] == true) init() else finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) init()
        else permLauncher.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))
    }

    @Suppress("MissingPermission")
    private fun init() {
        val lm  = getSystemService(LocationManager::class.java)
        val loc = lm?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val mLoc = loc?.let { MomentLocation(it.latitude, it.longitude, it.time) }
            ?: MomentLocation(0.0, 0.0, System.currentTimeMillis())

        lifecycleScope.launch {
            // pick one for demo… switch as needed:
            //localDataStore.loginAsAdmin()
            localDataStore.loginAsBasic()

            val user    = localDataStore.currentUser.firstOrNull()
            val userId  = user?.id
            val userName= user?.name ?: "User"

            // If admin → go to AdminView
            if (user?.isAdmin == true) {
                AdminViewActivity.launch(this@HomeActivity, userName)
            } else {
                // else regular flow
                val stop = busRepo.getBusStopForLocation(mLoc.latitude, mLoc.longitude)
                if (stop == null) {
                    NoStopActivity.launch(this@HomeActivity, mLoc, userId)
                } else {
                    AtStopActivity.launch(this@HomeActivity, mLoc, userId)
                }
            }
            finish()
        }
    }
}
