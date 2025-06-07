// app/src/main/java/fct/nova/beeppisca/ui/home/NoStopActivity.kt
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

class NoStopActivity : ComponentActivity() {

    private val viewModel: NoStopViewModel by viewModels()
    private val dataStore by lazy { LocalDataStore(appDataStore) }

    companion object {
        private const val EXTRA_LOC  = "extra_loc"
        private const val EXTRA_USER = "extra_user"
        fun launch(ctx: Context, loc: MomentLocation, userId: String?) {
            ctx.startActivity(Intent(ctx, NoStopActivity::class.java).apply {
                putExtra(EXTRA_LOC, loc)
                putExtra(EXTRA_USER, userId)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loc = intent.getParcelableExtra(EXTRA_LOC, MomentLocation::class.java)
        val userId = intent.getStringExtra(EXTRA_USER)
        requireNotNull(loc) { "MomentLocation must be passed to NoStopActivity" }

        lifecycleScope.launch {
            userId?.let { viewModel.loadUserTickets(it) }
            setContent {
                BeepPiscaTheme {
                    NoStopScreen(
                        viewModel = viewModel,
                        userLoc = loc,
                        userId = userId,
                        onBuy = { /* … */ },
                        onSearch = { /* … */ },
                        onSettings = {}
                    )
                }
            }
        }
    }
}
