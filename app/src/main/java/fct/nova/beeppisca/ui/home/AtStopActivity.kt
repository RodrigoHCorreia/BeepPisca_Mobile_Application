package fct.nova.beeppisca.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import fct.nova.beeppisca.domain.MomentLocation
import fct.nova.beeppisca.storage.LocalDataStore
import fct.nova.beeppisca.ui.theme.BeepPiscaTheme
import kotlinx.coroutines.launch

private val Context.appDataStore by preferencesDataStore("user_prefs")

class AtStopActivity : ComponentActivity() {

    private val viewModel: AtStopViewModel by viewModels()
    private val dataStore  by lazy { LocalDataStore(appDataStore) }

    companion object {
        private const val EXTRA_LOC  = "extra_loc"
        private const val EXTRA_USER = "extra_user"
        fun launch(ctx: Context, loc: MomentLocation, userId: String?) {
            ctx.startActivity(Intent(ctx, AtStopActivity::class.java).apply {
                putExtra(EXTRA_LOC, loc)
                putExtra(EXTRA_USER, userId)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // On API 33+:
        val loc: MomentLocation? =
            intent.getParcelableExtra(EXTRA_LOC, MomentLocation::class.java)
        // On older APIs you can still use the generic getter:
        // val loc = intent.getParcelableExtra<MomentLocation>(EXTRA_LOC)

        val userId = intent.getStringExtra(EXTRA_USER)

        requireNotNull(loc) { "MomentLocation must be passed to AtStopActivity" }

        lifecycleScope.launch {
            userId?.let { viewModel.loadForLocation(it, loc) }
            setContent {
                BeepPiscaTheme {
                    AtStopScreen(
                        viewModel = viewModel,
                        userLoc   = loc,
                        userId    = userId,
                        onBuy     = { /* … */ },
                        onBack    = { NoStopActivity.launch(this@AtStopActivity, loc, userId) }
                    )
                }
            }
        }
    }
}
